package org.cooltutors.student.ui.openings;
// Darren Stults

import android.view.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.cooltutors.student.R;

import java.util.List;

public class OpeningRecyclerAdapter extends RecyclerView.Adapter<OpeningRecyclerAdapter.OpeningViewHolder>{

    //region interfaces, declarations, constructors

    // interfaces

    public interface OpeningItemClickListener {
        void onItemClick(Opening opening, View view);
    }

    // declarations

    private final List<Opening> openingList;
    final private OpeningItemClickListener clickListener;

    // constructors

    public OpeningRecyclerAdapter(List<Opening> openingList, OpeningItemClickListener clickListener) {
        this.openingList = openingList;
        this.clickListener = clickListener;
    }

    // endregion

    // internal classes

    class OpeningViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView openingId;
        private final TextView openingDate;
        private final TextView openingStart;
        private final TextView openingInstructor;
        private final TextView openingDayOfWeek;
        private final CardView openingCard;

        public OpeningViewHolder(View view, OpeningRecyclerAdapter adapter) {
            super(view);

            // declarations
            this.openingId = view.findViewById(R.id.opening_id);
            this.openingDate = view.findViewById(R.id.opening_date);
            this.openingStart = view.findViewById(R.id.opening_time);
            this.openingInstructor = view.findViewById(R.id.opening_instructor);
            this.openingDayOfWeek = view.findViewById(R.id.opening_day_of_week);
            this.openingCard = view.findViewById(R.id.opening_card);

            // listener
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Opening opening = openingList.get(getAdapterPosition());
            clickListener.onItemClick(opening, view);
        }
    }

    //region adapter methods

    @Override
    public OpeningRecyclerAdapter.OpeningViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.opening_item, viewGroup, false);
        return new OpeningViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull OpeningRecyclerAdapter.OpeningViewHolder holder, int position) {
        Opening opening = openingList.get(position);

        holder.openingId.setText(opening.id);
        holder.openingDate.setText(opening.date);
        holder.openingStart.setText(String.format("%s - %s", opening.startTime, opening.endTime));
        holder.openingInstructor.setText(opening.instructorName);
        holder.openingDayOfWeek.setText(opening.dayOfWeek);
        holder.openingDayOfWeek.setTextColor(opening.foregroundColor);
        holder.openingCard.setCardBackgroundColor(opening.backgroundColor);
    }

    @Override
    public int getItemCount() {
        if (openingList == null) return 0;
        return openingList.size();
    }

    //endregion

}
