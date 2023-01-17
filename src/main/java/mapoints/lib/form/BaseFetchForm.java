package mapoints.lib.form;

public abstract class BaseFetchForm extends BaseForm{
    private String query;
    private Integer pageNum;
    private Integer pageSize;

    public String getQuery() {
        return query == null ? "" : query.trim();
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getPageNum() {
        return pageNum == null
                ? 0
                : pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize == null
                ? 25
                : pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
