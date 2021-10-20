package util;

import java.util.List;
import java.util.Objects;

public interface ListSafeAccessor {

    default <E> E getAtIndexOrElse(List<E> list, int index, E alternative) {
        Objects.requireNonNull(list);
        return index >= 0 && index < list.size()
                ? list.get(index)
                : alternative;
    }
}
