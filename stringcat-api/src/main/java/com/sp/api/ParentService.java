package com.sp.api;

import com.sp.domain.domain.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
public abstract class ParentService<T, ID extends Serializable> {

    @Autowired
    protected UserRepository _userRepository;

    public boolean isEmpty(String value) {
        return StringUtils.isEmpty(value);
    }

    public boolean isNotEmpty(String value) { return !isEmpty(value); }

    public boolean isEmpty(Collection<?> list) {
        return isNull(list) || list.size() == 0;
    }

    public boolean isNotEmpty(Collection<?> list) { return !isEmpty(list); }

    public boolean isNull(Object o) { return Objects.isNull(o); }

    public boolean isNotNull(Object o) { return !isNull(o); }

    public boolean equals(Object o1, Object o2) {
        if (isNull(o1) || isNull(o2)) {
            return false;
        } else {
            return o1.equals(o2);
        }
    }

    public boolean isEquals(String str1, String str2) {
        return StringUtils.equals(str1, str2);
    }

    public boolean isNotEquals(String str1, String str2) {
        return !StringUtils.equals(str1, str2);
    }

    public boolean notEquals(Object o1, Object o2) {
        return !this.equals(o1, o2);
    }

    public String likeLeft(String str) {
        return "%" + str;
    }

    public String likeRight(String str) {
        return str + "%";
    }

    public String likeBoth(String str) {
        return "%" + str + "%";
    }

    public static <T> Stream<T> asStream(Collection<T> collection) {
        return collection == null ? Stream.empty() : collection.stream();
    }

    public static <T> List<T> toList(final Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

}
