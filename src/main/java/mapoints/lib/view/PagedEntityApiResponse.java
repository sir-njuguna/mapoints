package mapoints.lib.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagedEntityApiResponse<T> extends EntityApiResponse<T> {
    @Getter
    @Setter
    private Long totalElementCount;

    @Getter
    @Setter
    private int pageCount;

    @Getter
    @Setter
    private int pageSize;

    @Getter
    @Setter
    private int pageNum;

    @Getter
    @Setter
    private boolean hasPrevious;

    @Getter
    @Setter
    private boolean hasNext;

    public PagedEntityApiResponse() {}

    public PagedEntityApiResponse(Page<?> page, T data) {
        setData(data);
        setTotalElementCount(page.getTotalElements());
        setPageCount(page.getTotalPages());
        setPageSize(page.getSize());
        setPageNum(page.getNumber());
        setHasPrevious(page.hasPrevious());
        setHasNext(page.hasNext());
    }

    public PagedEntityApiResponse(boolean status, int statusCode, String message, T data) {
        super(status, statusCode, message, data);
    }
}
