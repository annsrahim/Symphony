package demo.acube.application.healthcare.activity.interfaces;

/**
 * Created by Anns on 11/07/17.
 */

public interface ICallRequestListener {
    public void onCallRequestCompleted();
    public void onCallRequestFailed(String msg);
}
