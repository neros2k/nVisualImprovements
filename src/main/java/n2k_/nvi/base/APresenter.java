package n2k_.nvi.base;
import org.bukkit.event.Listener;
public abstract class APresenter implements Listener {
    private final APlugin PLUGIN;
    public APresenter(APlugin PLUGIN) {
        this.PLUGIN = PLUGIN;
    }
    public abstract void init();
    public APlugin getPlugin() {
        return this.PLUGIN;
    }
}
