package extensions.ch.glue.grid.utils;

public abstract class DaoWrapper<DAO, ITEM>
{
  protected DAO dao; 
  
  public DaoWrapper(DAO d)
  {
    this.dao = d; 
  }
  public abstract ITEM persist();
  
  public abstract ITEM merge(ITEM item); 
  
  public abstract ITEM update(ITEM item); 
}
