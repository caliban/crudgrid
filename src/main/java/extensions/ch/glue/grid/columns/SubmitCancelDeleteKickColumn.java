

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
public abstract class SubmitCancelDeleteKickColumn<T> extends SubmitCancelDeleteColumn<T>
{

  private static final long serialVersionUID = 1L;

  private static final Logger log = Logger.getLogger(SubmitCancelDeleteKickColumn.class);

  public SubmitCancelDeleteKickColumn(String columnId, IModel<String> headerModel)
  {
    super(columnId, headerModel);
    // grösse ändern
    setInitialSize(80);
  }

  /**
   * ziemlich unverändert, nur die erweiterung ums icon und deleteaction
   */
  @Override
  public Component newCell(WebMarkupContainer parent, String componentId,
      final IModel rowModel)
  {
    return new SubmitCancelDeleteKickPanel(componentId, rowModel, getGrid())
    {

      private static final long serialVersionUID = 1L;

      private WebMarkupContainer getRowComponent()
      {
        return getGrid().findParentRow(this);
      };

      @Override
      protected void onCancel(AjaxRequestTarget target)
      {
        SubmitCancelDeleteKickColumn.this.onCancel(target, rowModel, getRowComponent());
      }

      @Override
      protected void onError(AjaxRequestTarget target)
      {
        SubmitCancelDeleteKickColumn.this.onError(target, rowModel, getRowComponent());
      }

      @Override
      protected void onSubmitted(AjaxRequestTarget target)
      {
        SubmitCancelDeleteKickColumn.this.onSubmitted(target, rowModel, getRowComponent());
      }

      @Override
      protected Icon getSubmitIcon()
      {
        return SubmitCancelDeleteKickColumn.this.getSubmitIcon();
      }

      @Override
      protected Icon getCancelIcon()
      {
        return SubmitCancelDeleteKickColumn.this.getCancelIcon();
      }

      @Override
      protected Icon getDeleteIcon()
      {
        return SubmitCancelDeleteKickColumn.this.getDeleteIcon();
      }

      @Override
      protected void onDelete(AjaxRequestTarget target)
      {
        SubmitCancelDeleteKickColumn.this.onDelete(target, rowModel, getRowComponent());
      }

	@Override
	protected void onKick(AjaxRequestTarget target) {
		SubmitCancelDeleteKickColumn.this.onKick(target, rowModel, getRowComponent());
		
	}

	@Override
	protected Icon getKickIcon() {
		return Icons.KICK;
	}
    };
  }

protected abstract void onKick(AjaxRequestTarget target, IModel<T> rowModel,
		WebMarkupContainer rowComponent) ;

}
