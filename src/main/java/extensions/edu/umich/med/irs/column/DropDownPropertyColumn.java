package extensions.edu.umich.med.irs.column;

import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import com.inmethod.grid.column.editable.EditablePropertyColumn;

/**
 * 
 * @author suilix http://old.nabble.com/user/UserProfile.jtp?user=1589611
 */
public class DropDownPropertyColumn<RT, CT> extends EditablePropertyColumn
{
  private static final long serialVersionUID = 1L;

  private List<CT> choices;

  protected List<CT> getChoices()
  {
    return choices;
  }

  protected void setChoices(List<CT> choices)
  {
    this.choices = choices;
  }

  private ChoiceRenderer<CT> renderer;

  protected ChoiceRenderer<CT> getRenderer()
  {
    return renderer;
  }

  protected void setRenderer(ChoiceRenderer<CT> renderer)
  {
    this.renderer = renderer;
  }

  private DropDownPropertyColumn<RT, CT> getGridColumn()
  {
    return this;
  }

  @SuppressWarnings("unchecked")
  private IModel<RT> rowTypeModel(IModel<?> rowModel)
  {
    return (IModel<RT>) rowModel;
  }

  @SuppressWarnings("unchecked")
  private IModel<CT> cellTypeModel(IModel<?> cellModel)
  {
    return (IModel<CT>) cellModel;
  }

  @SuppressWarnings("unchecked")
  protected IModel getFieldModel(IModel rowModel)
  {
    return new PropertyModel<RT>(rowTypeModel(rowModel), getPropertyExpression());
  }

  @SuppressWarnings("unchecked")
  @Override
  protected DropDownPanel newCellPanel(String componentId, IModel rowModel,
      IModel cellModel)
  {
    return new DropDownPanel(componentId, rowTypeModel(rowModel),
        cellTypeModel(cellModel), getGridColumn(), getChoices(), getRenderer());
  }

  @SuppressWarnings("unchecked")
  @Override
  public Component newCell(WebMarkupContainer parent, String componentId, IModel rowModel)
  {
    DropDownPanel panel = newCellPanel(componentId, rowTypeModel(rowModel),
        getFieldModel(rowModel));
    addValidators(panel.getEditComponent());
    return panel;
  }

  public DropDownPropertyColumn(IModel<String> headerModel, String propertyExpression,
      String sortProperty, List<CT> choices, ChoiceRenderer<CT> renderer)
  {
    super(headerModel, propertyExpression, sortProperty);
    setChoices(choices);
    setRenderer(renderer);
  }
}
// vi:set tabstop=4 hardtabs=4 shiftwidth=4:
