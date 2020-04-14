package com.jetbrains.common.response.storage;

import java.io.Serializable;
import java.util.Collection;

public class GetNamesByIdsResponse implements Serializable {
    private Collection<String> names;

    public GetNamesByIdsResponse() {
    }

    public GetNamesByIdsResponse(Collection<String> names) {
        this.names = names;
    }

    public Collection<String> getNames() {
        return names;
    }

    public void setNames(Collection<String> names) {
        this.names = names;
    }
}
