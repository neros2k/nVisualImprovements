package n2k_.nvi;
import n2k_.nvi.base.APlugin;
import n2k_.nvi.explosions.TNTListener;
public final class nVisualImprovements extends APlugin {
    @Override
    public void onEnable() {
        new TNTListener(this).init();
    }
}
