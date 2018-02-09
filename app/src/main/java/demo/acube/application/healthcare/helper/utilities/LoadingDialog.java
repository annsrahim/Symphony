package demo.acube.application.healthcare.helper.utilities;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Anns on 28/04/17.
 */

public class LoadingDialog {

    public static ProgressDialog show(Context context, String message)
    {
        ProgressDialog m_Dialog = new ProgressDialog(context);
        m_Dialog.setMessage(message);
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();
        return m_Dialog;

    }
}
