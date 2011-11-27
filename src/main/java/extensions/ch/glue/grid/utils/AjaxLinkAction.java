package extensions.ch.glue.grid.utils;

import java.io.Serializable;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

public interface AjaxLinkAction extends Serializable
{

  void onClick(AjaxRequestTarget arg0, IModel<?> rowModel);

}
