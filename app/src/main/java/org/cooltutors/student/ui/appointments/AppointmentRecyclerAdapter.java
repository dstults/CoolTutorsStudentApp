package org.cooltutors.student.ui.appointments;
// Darren Stults

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.cooltutors.student.R;

import java.util.List;

public class AppointmentRecyclerAdapter extends RecyclerView.Adapter<AppointmentRecyclerAdapter.AppointmentViewHolder>{

    //region interfaces, declarations, constructors

    // interfaces

    public interface AppointmentItemClickListener {
        void onItemClick(Appointment appointment, View view);
    }

    // declarations

    private final List<Appointment> appointmentList;
    final private AppointmentItemClickListener bookClickListener;

    // constructors

    public AppointmentRecyclerAdapter(List<Appointment> appointmentList, AppointmentItemClickListener bookClickListener) {
        this.appointmentList = appointmentList;
        this.bookClickListener = bookClickListener;
    }

    // endregion

    // internal classes

    class AppointmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView appointmentId;
        private final TextView appointmentDate;
        private final TextView appointmentStart;
        private final TextView appointmentEnd;
        private final TextView appointmentStudent;
        private final TextView appointmentInstructor;
        private final TextView appointmentDayOfWeek;
        private final CardView appointmentCard;

        public AppointmentViewHolder(View view, AppointmentRecyclerAdapter adapter) {
            super(view);

            // declarations
            this.appointmentId = view.findViewById(R.id.appointment_id);
            this.appointmentDate = view.findViewById(R.id.appointment_date);
            this.appointmentStart = view.findViewById(R.id.appointment_start);
            this.appointmentEnd = view.findViewById(R.id.appointment_end);
            this.appointmentStudent = view.findViewById(R.id.appointment_student);
            this.appointmentInstructor = view.findViewById(R.id.appointment_instructor);
            this.appointmentDayOfWeek = view.findViewById(R.id.appointment_day_of_week);
            this.appointmentCard = view.findViewById(R.id.appointment_card);

            // listener
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Appointment appointment = appointmentList.get(getAdapterPosition());
            bookClickListener.onItemClick(appointment, view);
        }
    }

    //region adapter methods

    @Override
    public AppointmentRecyclerAdapter.AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.appointment_item, viewGroup, false);
        return new AppointmentViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentRecyclerAdapter.AppointmentViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);

        holder.appointmentId.setText(appointment.id);
        holder.appointmentDate.setText(appointment.date);
        holder.appointmentStart.setText(appointment.startTime);
        holder.appointmentEnd.setText(appointment.endTime);
        holder.appointmentStudent.setText(appointment.studentName);
        holder.appointmentInstructor.setText(appointment.instructorName);
        holder.appointmentDayOfWeek.setText(appointment.dayOfWeek);
        holder.appointmentDayOfWeek.setTextColor(appointment.foregroundColor);
        holder.appointmentCard.setCardBackgroundColor(appointment.backgroundColor);
    }

    @Override
    public int getItemCount() {
        if (appointmentList == null) return 0;
        return appointmentList.size();
    }

    //endregion

}
