package dev.vality.alert.tg.bot.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Parameter implements Comparable<Parameter> {

    private final Set<String> values = new HashSet<>();
    private String id;
    private String name;

    @Override
    public int compareTo(Parameter o) {
        return Integer.valueOf(this.id)
                .compareTo(Integer.valueOf(o.getId()));
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Parameter parameter = (Parameter) o;

        return name.equals(parameter.name);
    }
}
