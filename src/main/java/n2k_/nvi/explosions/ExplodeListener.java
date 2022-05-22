package n2k_.nvi.explosions;
import n2k_.nvi.base.APlugin;
import n2k_.nvi.base.APresenter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.NotNull;
public class ExplodeListener extends APresenter implements Listener {
    public ExplodeListener(APlugin PLUGIN) {
        super(PLUGIN);
    }
    @Override
    public void init() {
        super.getPlugin().getServer().getPluginManager().registerEvents(this, super.getPlugin());
    }
    @EventHandler
    public void onExplosion(@NotNull EntityExplodeEvent EVENT) {
        EVENT.setCancelled(true);
        new BlastWave(EVENT.getLocation(), 15, super.getPlugin()).start();
        new ExplosiveLine(EVENT.getLocation(), 5, super.getPlugin(), EVENT.blockList()).start();
    }
}
