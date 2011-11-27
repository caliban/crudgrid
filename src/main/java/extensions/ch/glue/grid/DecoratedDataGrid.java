package extensions.ch.glue.grid;

import java.util.List;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import com.inmethod.grid.DataProviderAdapter;
import com.inmethod.grid.IGridColumn;
import com.inmethod.grid.common.AbstractGrid;
import com.inmethod.grid.datagrid.DefaultDataGrid;

/**
 * datagrid erweitert. er umfasst nun zusätzlich einen addbutton wo Objekte
 * hinzugefügt werden können. müssen den gleichen typ haben wie der rest des
 * grids.
 * 
 */
@SuppressWarnings("serial")
public abstract class DecoratedDataGrid<T> extends DefaultDataGrid
{
  protected List<T> rawList;

  private static final Logger log = Logger.getLogger(DecoratedDataGrid.class);

  /**
   * haupteintritt
   * 
   * @param id
   *          wicketid für den grid
   * @param title
   *          unter was für einem namen der grid firmieren soll
   * @param buttonTitle
   *          String model for the add button
   * @param list
   *          die liste der zeilenobjekte
   * @param columns
   *          spalten(property, editable was auch immer)
   * @param modelAsClass
   *          die klasse des modells (also eines objektes in der gegebenen Liste
   */
  public DecoratedDataGrid(String id, IModel<String> title, IModel<String> buttonTitle, List<T> list,
      List<IGridColumn> columns)
  {
    this(id, title, buttonTitle, new DataProviderAdapter(new ListDataProvider(list)),
        columns);
    this.rawList = list;
  }
  /**
   * 
   * invoke this constructor only if you have a custom dataprovideradapter. else
   * use the constructor with the list.
   */
  protected DecoratedDataGrid(String id, IModel<String> title, IModel<String> buttonTitle,
      DataProviderAdapter list, List<IGridColumn> columns)
  {
    super(id, list, columns);
    super.add(new Label("gridtitle",title));
    super.add(new AjaxButton("add", buttonTitle)
    {

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form)
      {
        onAddRow(target, form);
        update();
        target.addComponent(getGrid());

      }
    }).setOutputMarkupId(true);
  }

  public AbstractGrid getGrid()
  {
    return this;
  }

  /**
   * what should happen if you press submit on the add button
   */
  protected abstract void onAddRow(AjaxRequestTarget target, Form<?> form);


}
