package fi.natroutter.realchairs.files;

import fi.natroutter.realchairs.RealChairs;
import fi.natroutter.natlibs.config.IConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@AllArgsConstructor
public enum Config implements IConfig {


    LANGUAGE("General.Language"),
    USE_MESSAGES("General.UseMessages")



    ;

    @Getter
    String path;

    @Override
    public JavaPlugin getPlugin() {
        return RealChairs.getInstance();
    }
}
