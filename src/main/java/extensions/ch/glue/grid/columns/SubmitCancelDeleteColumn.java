

package extensions.ch.glue.grid.columns;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import com.inmethod.grid.column.editable.SubmitCancelColumn;
import com.inmethod.grid.common.Icons;
import com.inmethod.icon.Icon;

/**
 * SubmitCancelColumn von inmethod-grid als grundlage erweitert um die
 * möglichkeit auch ein objekt (und damit ein db eintrag) zu löschen.
 * 
 * @author pengt
 * 
 */
public abstract class SubmitCancelDeleteColumn<T> extends SubmitCancelColumn
{

  private static final long serialVersionUID = 1L;

  private static final Logger log = Logger.getLogger(SubmitCancelDeleteColumn.class);

  public SubmitCancelDeleteColumn(String columnId, IModel<String> headerModel)
  {
    super(columnId, headerModel);
    // grösse ändern
    setInitialSize(66);
  }

  /**
   * ziemlich unverändert, nur die erweiterung ums icon und deleteaction
   */
  @Override
  public Component newCell(WebMarkupContainer parent, String componentId,
      final IModel rowModel)
  {
    return new SubmitCancelDeletePanel(componentId, rowModel, getGrid())
    {

      private static final long serialVersionUID = 1L;

      private WebMarkupContainer getRowComponent()
      {
        return getGrid().findParentRow(this);
      };

      @Override
      protected void onCancel(AjaxRequestTarget target)
      {
        SubmitCancelDeleteColumn.this.onCancel(target, rowModel, getRowComponent());
      }

      @Override
      protected void onError(AjaxRequestTarget target)
      {
        SubmitCancelDeleteColumn.this.onError(target, rowModel, getRowComponent());
      }

      @Override
      protected void onSubmitted(AjaxRequestTarget target)
      {
        SubmitCancelDeleteColumn.this.onSubmitted(target, rowModel, getRowComponent());
      }

      @Override
      protected Icon getSubmitIcon()
      {
        return SubmitCancelDeleteColumn.this.getSubmitIcon();
      }

      @Override
      protected Icon getCancelIcon()
      {
        return SubmitCancelDeleteColumn.this.getCancelIcon();
      }

      @Override
      protected Icon getDeleteIcon()
      {
        return SubmitCancelDeleteColumn.this.getDeleteIcon();
      }

      @Override
      protected void onDelete(AjaxRequestTarget target)
      {
        SubmitCancelDeleteColumn.this.onDelete(target, rowModel, getRowComponent());
      }
    };
  }
  /**
   * neu, damit wir das icon kriegen (liegt übrigens schon im inmethod jar.
   * wurde aber durch glue gezeichnet. (bzw zwei zeichnungen von inmethod
   * kombiniert)
   */
  protected Icon getDeleteIcon()
  {
    return Icons.DELETE;
  }


  protected void onDelete(AjaxRequestTarget target, IModel rowModel,
      WebMarkupContainer rowComponent) {
    onCustomDelete(target, rowModel, rowComponent);
    getGrid().setItemEdit(rowModel, false);
    getGrid().update();
    target.addComponent(getGrid());
  }

  @Override
  protected void onSubmitted(AjaxRequestTarget target, IModel rowModel,
      WebMarkupContainer rowComponent) {
    onCustomSubmitted(target, rowModel, rowComponent);
    getGrid().setItemEdit(rowModel, false);
    getGrid().update();
    target.addComponent(getGrid());
  }
 

  @Override
  protected void onCancel(AjaxRequestTarget target, IModel rowModel,
      WebMarkupContainer rowComponent)
  {
    onCustomCancel(target, rowModel, rowComponent);
    getGrid().setItemEdit(rowModel, false);
    getGrid().update();
    target.addComponent(getGrid());
  }
 
  /**
   * ausprogrammieren fürs löschen eines Objektes
   */
  protected abstract void onCustomDelete(AjaxRequestTarget target, IModel<T> rowModel,
      WebMarkupContainer rowComponent);

 /**
  * ausprogrammieren fürs speichern eines objektes 
  */
  protected abstract void onCustomSubmitted(AjaxRequestTarget target, IModel<T> rowModel,
      WebMarkupContainer rowComponent);
 
  /**
   * Ausprogrammieren für ein "undo"   
   * 
   * Falls ein linkuploadcolumn verwendet wurde, muss zwingend das modelobjekt neu 
   * aus dem db layer gezogen werden. 
   */
  protected abstract void onCustomCancel(AjaxRequestTarget target, IModel<T> rowModel,
      WebMarkupContainer rowComponent);

}
