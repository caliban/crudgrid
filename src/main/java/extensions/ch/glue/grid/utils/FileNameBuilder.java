package extensions.ch.glue.grid.utils;

import java.io.Serializable;
import org.apache.wicket.model.IModel;

public interface FileNameBuilder extends Serializable
{
  public String getFileName(IModel<?> model);

}
