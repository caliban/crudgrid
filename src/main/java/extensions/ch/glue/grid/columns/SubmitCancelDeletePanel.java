package extensions.ch.glue.grid.columns;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import com.inmethod.grid.column.editable.SubmitCancelPanel;
import com.inmethod.grid.common.AbstractGrid;
import com.inmethod.icon.Icon;
import com.inmethod.icon.IconImage;

/**
 * standard submitcancelpanel das um einen knopf erweitert wurde. 
 * @author pengt
 *
 */
@SuppressWarnings("serial")
public abstract class SubmitCancelDeletePanel extends SubmitCancelPanel {

    SubmitCancelDeletePanel(String id, final IModel model, AbstractGrid grid) {
        super(id, model, grid);


        //fügt den delete knopf ein. gleiches verhalten wie der submit und
        // cancel button der schon existierte. 
        AjaxLink<?> delete = new AjaxLink<Object>("delete") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                onDelete(target);
            }

            @Override
            public boolean isVisible() {
                return getGrid().isItemEdited(model);
            }
        };
        add(delete);
        delete.add(new IconImage("icon", getDeleteIcon()));

    }

    protected abstract void onDelete(AjaxRequestTarget target);

    protected abstract Icon getDeleteIcon();

    public AbstractGrid getGrid()
    {
        return findParent(AbstractGrid.class);
    }
}
