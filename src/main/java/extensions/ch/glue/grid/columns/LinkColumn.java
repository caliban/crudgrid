package extensions.ch.glue.grid.columns;

import java.util.Locale;
import org.apache.log4j.Logger;
import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.DynamicWebResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.lang.PropertyResolver;
import org.apache.wicket.util.string.Strings;
import com.inmethod.grid.column.AbstractColumn;
import extensions.ch.glue.grid.utils.AjaxLinkAction;
import extensions.ch.glue.grid.utils.FileNameBuilder;

/**
 * 
 * @author pengt
 * 
 */
@SuppressWarnings("serial")
public class LinkColumn extends AbstractColumn
{
  private static final Logger log = Logger.getLogger(LinkColumn.class);

  // name des feld (auf dem pojo)

  private boolean escapeMarkup;

  private String nameExpression;

  private AjaxLinkAction action;

  public LinkColumn(String columnId, IModel<?> headerModel, String nameExpression,
      AjaxLinkAction action)
  {
    super(columnId, headerModel);
    // grösse anpassen
    super.setInitialSize(75);
    // property expression speichern
    this.action = action;
    this.nameExpression = nameExpression;
  }

  /**
   * liefert eine neue Zelle pro rowmodel
   */
  @Override
  public Component newCell(WebMarkupContainer parent, String componentId, IModel rowModel)
  {
    return new LinkPanel(componentId, rowModel);
  }

  /**
   * geklaut von inmethod-grid PropertyColumn
   */
  private CharSequence getValue(IModel<?> rowModel, String expression)
  {
    Object rowObject = getModelObject(rowModel);
    Object property = null;
    if (rowObject != null)
    {
      try
      {
        property = getProperty(rowObject, expression);
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

  private byte[] getValueAsByteArray(IModel<?> rowModel, String expression)
  {
    Object rowObject = getModelObject(rowModel);
    Object property = null;
    if (rowObject != null)
    {
      try
      {
        property = getProperty(rowObject, expression);
      } catch (NullPointerException e)
      {

      }
    }

    return (byte[]) property;
  }

  /**
   * geklaut von inmethod-grid PropertyColumn
   */
  protected Object getModelObject(IModel<?> rowModel)
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
  class LinkPanel extends Panel
  {
    public LinkPanel(String id, final IModel<?> rowModel)
    {
      super(id);
      this.setOutputMarkupId(true);

      AjaxLink<Void> link = null;

      add(link = new AjaxLink<Void>("link")
      {
        @Override
        public void onClick(AjaxRequestTarget arg0)
        {
          action.onClick(arg0, rowModel);
        }
      });
      link.add(new Label("label", (String) getValue(rowModel, nameExpression)));
    }
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
}
