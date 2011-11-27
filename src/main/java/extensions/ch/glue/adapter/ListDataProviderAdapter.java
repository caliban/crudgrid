package extensions.ch.glue.adapter;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import com.inmethod.grid.DataProviderAdapter;

public class ListDataProviderAdapter extends DataProviderAdapter {

  IDataProvider provider; 
  
  public ListDataProviderAdapter(IDataProvider dataProvider)
  {
    super(dataProvider);
    this.provider = dataProvider; 
    
  }
  public IDataProvider getProvider()
  {
    return provider; 
  }
}
