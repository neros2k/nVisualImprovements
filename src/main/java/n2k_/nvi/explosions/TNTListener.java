package n2k_.nvi.explosions;
import n2k_.nvi.base.APlugin;
import n2k_.nvi.base.APresenter;
import org.bukkit.block.data.type.TNT;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.NotNull;
public class TNTListener extends APresenter implements Listener {
    public TNTListener(APlugin PLUGIN) {
        super(PLUGIN);
    }
    @Override
    public void init() {
        super.getPlugin().getServer().getPluginManager().registerEvents(this, super.getPlugin());
    }
    @EventHandler
    public void onTntExplosion(@NotNull EntityExplodeEvent EVENT) {
        new BlastWave(EVENT.getLocation(), 10).start();
    }
}
