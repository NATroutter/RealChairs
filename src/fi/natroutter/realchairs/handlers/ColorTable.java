package fi.natroutter.realchairs.handlers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Color;

@AllArgsConstructor
public enum ColorTable {

    ADDED(Color.fromRGB(0, 255, 64)),
    REMOVED(Color.fromRGB(245, 22, 44)),
    DISPLAY(Color.fromRGB(247, 15, 174)),

    ;

    @Getter
    private final Color color;

}
