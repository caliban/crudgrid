package extensions.edu.umich.med.irs.column;

import java.util.List;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;

import com.inmethod.grid.column.AbstractColumn;
import com.inmethod.grid.column.editable.EditableCellPanel;
/**
 * All classes in this package were posted as an example on
 * http://old.nabble.com/inmethod-datagrid-with-a-DropDownChoice-cell-td22068863.html
 * 
 * @author suilix
 * http://old.nabble.com/user/UserProfile.jtp?user=1589611
 * 
 */
public class DropDownPanel<RT,CT>
	extends EditableCellPanel
{
	private static final long serialVersionUID = 1L;

	public DropDownPanel(String id, IModel<RT> rowModel, IModel<RT> cellModel, 
		AbstractColumn gridColumn, final List<CT> choices, final ChoiceRenderer<CT> renderer)
	{
		super(id, gridColumn, rowModel);
		DropDownChoice<CT> ddc = 
			new DropDownChoice<CT>("dropdown", choices, renderer)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onComponentTag(ComponentTag tag)
				{
					super.onComponentTag(tag);
					if (isValid() == false)
					{
						tag.put("class", "imxt-invalid");
						FeedbackMessage message = getFeedbackMessage();
						if (message != null)
						{
							tag.put("title", message.getMessage().toString());
						}
					}
				}
			};
		ddc.setDefaultModel(cellModel);
		ddc.setOutputMarkupId(true);
		ddc.setLabel(getDropDownChoiceLabel(gridColumn));
		add(ddc);
	}

	@SuppressWarnings("unchecked")
	private IModel<String> getDropDownChoiceLabel(AbstractColumn gridColumn)
	{
		return gridColumn.getHeaderModel();
	}

	@SuppressWarnings("unchecked")
	@Override
	public FormComponent<CT> getEditComponent()
	{
		return (FormComponent<CT>) get("dropdown");
	}
}
// vi:set tabstop=4 hardtabs=4 shiftwidth=4: