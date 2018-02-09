package demo.acube.application.healthcare.activity.patient.acitivity.myTeleHealthTeam;

/**
 * Created by Anns on 19/07/17.
 */

public interface IExpandableChildItemListener {
    public void goToCallOperation(int groupPos);
    public void goToChatOperation(int groupPos);
    public void goToVideoOpeartion(int groupPos);
    public void goToScheduleAppointment(int groupPos);
}
