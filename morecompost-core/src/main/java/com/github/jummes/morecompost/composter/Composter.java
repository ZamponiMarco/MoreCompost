package com.github.jummes.morecompost.composter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.SerializableAs;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.Model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SerializableAs("Composter")
public class Composter implements Model {

    @EqualsAndHashCode.Include
    @Serializable(stringValue = true)
    private UUID id;
    @Serializable()
    private List<Location> composters;

    @SuppressWarnings("unchecked")
    public static Composter deserialize(Map<String, Object> map) {
        UUID id = UUID.fromString((String) map.get("id"));
        List<Location> composters = (List<Location>) map.get("composters");
        return new Composter(id, composters);
    }

}
