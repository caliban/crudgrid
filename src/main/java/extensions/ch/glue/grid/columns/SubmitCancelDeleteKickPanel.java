

package extensions.ch.glue.grid.columns;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import com.inmethod.grid.common.AbstractGrid;
import com.inmethod.icon.Icon;
import com.inmethod.icon.IconImage;
/**
 * standard submitcancelpanel das um einen knopf erweitert wurde. 
 * @author pengt
 *
 */
@SuppressWarnings("serial")
public abstract class SubmitCancelDeleteKickPanel extends SubmitCancelDeletePanel {

	SubmitCancelDeleteKickPanel(String id, final IModel model, AbstractGrid grid) {
		super(id,model, grid);


		//fügt den delete knopf ein. gleiches verhalten wie der submit und
		// cancel button der schon existierte. 
		AjaxLink<?> kick = new AjaxLink<Object>("kick") {
      
      private static final long serialVersionUID = 1L;

      @Override
      public void onClick(AjaxRequestTarget target) {
        onKick(target);
      }
  
      @Override
      public boolean isVisible() {
        return getGrid().isItemEdited(model);
      }     
    };
    add(kick);
    kick.add(new IconImage("icon", getKickIcon()));
		
	}
	
	protected abstract void onKick(AjaxRequestTarget target);

 
	protected abstract Icon getKickIcon();
        
        
        

}
