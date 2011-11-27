package extensions.ch.glue.grid.columns;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.lang.PropertyResolver;
import org.apache.wicket.util.string.Strings;
import com.inmethod.grid.column.AbstractColumn;

/**
 * Hackwarnung! Diese Klassen macht Sachen die ausschliesslich in Firefox laufen
 * und auch nur dann wenn einige Änderungen im Firefox user.js gemacht wurden:
 * 
 * user_pref("capability.policy.policynames", "localfilelinks");
 * user_pref("capability.policy.localfilelinks.sites",
 * "http://<SERVER>:<PORT>");
 * user_pref("capability.policy.localfilelinks.checkloaduri.enabled",
 * "allAccess"); user_pref("signed.applets.codebase_principal_support", true);
 * user_pref("capability.principal.codebase.p0.granted", "UniversalFileRead");
 * user_pref("capability.principal.codebase.p0.id", "file://");
 * user_pref("capability.principal.codebase.p0.subjectName", "");
 * 
 * Dies ist eine Spalte die sofern kein wert hinterlegt wurde ein
 * fileuploadfield anbietet und sonst den wert als link rendert. Der Wert wird
 * anhand des fileuploads ermittelt. Dort wird der Pfad einer ausgewählten Datei
 * mittels javascript ausgelesen und mit einem Callback ans Wicket
 * zurückgegeben.
 * 
 * @author pengt
 * 
 */
@SuppressWarnings("serial")
public class LinkUploadColumn extends AbstractColumn
{
  private static final Logger log = Logger.getLogger(LinkUploadColumn.class);

  // name des feld (auf dem pojo)
  private String propertyExpression;

  private boolean escapeMarkup;

  public LinkUploadColumn(String columnId, IModel headerModel, String propertyexpression)
  {
    super(columnId, headerModel);
    // grösse anpassen
    super.setInitialSize(220);
    // property expression speichern
    this.propertyExpression = propertyexpression;
  }

  /**
   * liefert eine neue Zelle pro rowmodel
   */
  @Override
  public Component newCell(WebMarkupContainer parent, String componentId, IModel rowModel)
  {
    return new LinkUploadPanel(componentId, rowModel, propertyExpression);
  }

  /**
   * geklaut von inmethod-grid PropertyColumn
   */
  private CharSequence getValue(IModel rowModel)
  {
    Object rowObject = getModelObject(rowModel);
    Object property = null;
    if (rowObject != null)
    {
      try
      {
        property = getProperty(rowObject, getPropertyExpression());
      } catch (NullPointerException e)
      {

      }
    }
    CharSequence string = convertToString(property);
    if (isEscapeMarkup() && string != null)
    {
      string = Strings.escapeMarkup(string.toString());
    }
    return string;
  }

  /**
   * geklaut von inmethod-grid PropertyColumn
   */
  protected Object getModelObject(IModel rowModel)
  {
    return rowModel.getObject();
  }

  /**
   * geklaut von inmethod-grid PropertyColumn
   */
  public boolean isEscapeMarkup()
  {
    return escapeMarkup;
  }

  /**
   * panel das ein link und ein fileuploadfield umfasst
   * 
   * @author pengt
   * 
   */
  class LinkUploadPanel extends Panel
  {
    @SuppressWarnings("serial")
    public LinkUploadPanel(String id, final IModel rowModel, String prop)
    {
      super(id);
      this.setOutputMarkupId(true);

      // externer link für den "file"link
      String path = getValue(rowModel).toString();
      
      final ExternalLink link = new ExternalLink("filelink", path, getFileName(path))
      {
        // nur sichtbar sofern ein objekt (string) hinterlegt ist
        @Override
        public boolean isVisible()
        {
          return !Strings.isEmpty(LinkUploadColumn.this.getValue(rowModel));
        }
      };
      // muss via ajax anpassbar sein
      link.setOutputMarkupId(true);

      // fileuploadfield
      FileUploadField fileupload = new FileUploadField("fileupload", new Model())
      {
        // nur sichtbar sofern kein objekt vorhanden
        @Override
        public boolean isVisible()
        {
          return Strings.isEmpty(LinkUploadColumn.this.getValue(rowModel));
        }
      };
      add(fileupload);

      // magic! Diese Behavior fischt mittels ajax den vollen Filepfad ab und
      // liefert es
      // mit einem automatisch generierten callback script zurück an den server
      final AbstractDefaultAjaxBehavior getFullPath = new AbstractDefaultAjaxBehavior()
      {
        @Override
        protected void onComponentTag(ComponentTag tag)
        {
          super.onComponentTag(tag);
          // String js = "{wicketAjaxGet('" + getCallbackUrl() +
          // "&'+this.name+'='+wicketEncode(this.value)); return false;}";
          String javascript = "{try {"
              + " netscape.security.PrivilegeManager.enablePrivilege('UniversalFileRead');"
              + " } catch (err) {"
              + "  alert('Sorry, you can not enjoy this site because of ' +err+ '.');"
              + " return false;" + "}" + "wicketAjaxGet('" + getCallbackUrl()
              + "&fullpath='+this.value, function() { }, function() { });" + "}";
          tag.put("onchange", javascript);
        }

        // %%%alter script. funktioniert nicht wegen dem statischen
        // referenzieren%%%%
        // der zelle.
        // <script>
        // <!--
        // function savePath(element) {
        // // get more privileges
        // try {
        // netscape.security.PrivilegeManager.enablePrivilege("UniversalFileRead");
        // } catch (err) {
        // alert("Sorry, you can not enjoy this site because of " +err+ ".");
        // return false;
        // }
        //
        // // save path to hidden field
        // var wcall =
        // wicketAjaxGet('?wicket:interface=:0:tabs:panel:contractform:contractgrid:form:bodyContainer:body:row:4:item:fileupload:fileupload::IBehaviorListener:0:'
        // + '&fullpath='+element.value, function() { }, function() { });
        //            
        //            
        // }
        //            
        // //-->
        // </script>

        // Diese Metode nimmt den zurückgegebenen filepfad entgegen und
        // verwertet ihn weiter. zusätzlich werden noch via ajax die
        // komponenten neu gezeichnet
        protected void respond(final AjaxRequestTarget target)
        {
          log.debug("mark " + rowModel + " as editable");
          // grid neu zeichnen
          getGrid().setItemEdit(rowModel, true);
          getGrid().update();
          // string holen
          String fullPath = RequestCycle.get().getRequest().getParameter("fullpath");

          log.debug("received file name is: @" + fullPath + "@");
          // pfad bearbeiten
          // fullPath = fullPath.toLowerCase();
          // fullPath = fullPath.replace("g:", "file:///g:");
          if (!fullPath.startsWith("file://"))
          {
            fullPath = "file:///" + fullPath;
          }
          try
          {
            // speichern ins model (ist ja keine echte propertycolumn, tut nur
            // so als ob
            BeanUtils.setProperty(getModelObject(rowModel), getPropertyExpression(),
                fullPath);
          } catch (IllegalAccessException e)
          {
            log.fatal("illegalaccess: ", e);
          } catch (InvocationTargetException e)
          {
            log.fatal("illegalinvocation:", e);
          }
          // update link
          link.setDefaultModelObject(fullPath);
          LinkUploadPanel.this.addOrReplace(new ExternalLink("filelink", fullPath, getFileName(fullPath))
          {
            // nur sichtbar sofern ein objekt (string) hinterlegt ist
            @Override
            public boolean isVisible()
            {
              return !Strings.isEmpty(LinkUploadColumn.this.getValue(rowModel));
            }
          });
          target.addComponent(LinkUploadPanel.this);
        }
      };
      fileupload.add(getFullPath);
      add(link);
    }
  }

  /**
   * geklaut von inmethod-grid PropertyColumn
   */
  public String getPropertyExpression()
  {
    return propertyExpression;
  }

  /**
   * geklaut von inmethod-grid PropertyColumn
   */
  protected CharSequence convertToString(Object object)
  {
    if (object != null)
    {
      IConverter converter = getConverter(object.getClass());
      return converter.convertToString(object, getLocale());
    }
    else
    {
      return "";
    }
  }

  /**
   * geklaut von inmethod-grid PropertyColumn
   */
  protected IConverter getConverter(Class<?> type)
  {
    return Application.get().getConverterLocator().getConverter(type);
  }

  /**
   * geklaut von inmethod-grid PropertyColumn
   */
  protected Locale getLocale()
  {
    return Session.get().getLocale();
  }

  /**
   * geklaut von inmethod-grid PropertyColumn
   */
  protected Object getProperty(Object object, String propertyExpression)
  {
    return PropertyResolver.getValue(propertyExpression, object);
  }
  private String getFileName(String path)
  {
    String fileexp = "filelink";
    if (!Strings.isEmpty(path))
    {
      String[] pathSplitted = path.split("\\\\|/");
      // filenamen anzeigen
      if (!Strings.isEmpty(pathSplitted[pathSplitted.length - 1]))
      {
        fileexp = pathSplitted[pathSplitted.length - 1];
      }
    }
    return fileexp; 
  }

}
