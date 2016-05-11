package com.csc.lpaina.colloquium2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CardViewHolder> {

    private Cursor cursor;

    public RVAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    public void updateCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item, viewGroup, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i) {
        if (cursor.moveToPosition(i)) {
            cardViewHolder.title = cursor.getString(cursor.getColumnIndex(FeedsTable.COLUMN_TITLE));
            cardViewHolder.description = cursor.getString(cursor.getColumnIndex(FeedsTable.COLUMN_DESCRIPTION));
            cardViewHolder.checked = cursor.getInt(cursor.getColumnIndex(FeedsTable.COLUMN_STATUS)) > 0;
            cardViewHolder.range = cursor.getInt(cursor.getColumnIndex(FeedsTable.COLUMN_RANGE));

            cardViewHolder.textViewTitle.setText(cardViewHolder.title);
            cardViewHolder.textViewDescription.setText(cardViewHolder.description);
            cardViewHolder.checkBox.setChecked(cardViewHolder.checked);
            int star = cursor.getInt(cursor.getColumnIndex(FeedsTable.COLUMN_RANGE));
            int color;
            switch (star) {
                case 0:
                    color = R.color.task_0;
                    break;
                case 1:
                    color = R.color.task_1;
                    break;
                case 2:
                    color = R.color.task_2;
                    break;
                default:
                    color = R.color.task_default;
                    break;
            }

            cardViewHolder.cardView.setCardBackgroundColor(color);
            cardViewHolder.id = cursor.getInt(cursor.getColumnIndex(FeedsTable._ID));
        }
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        public static final String COLUMN_ID = "column_id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String RANGE = "range";
        public static final String CHECKED = "checked";

        final TextView textViewTitle;
        final TextView textViewDescription;
        final CheckBox checkBox;
        final CardView cardView;

        final Context context;

        int id;
        String title;
        String description;
        boolean checked;
        int range;

        CardViewHolder(View itemView) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.title);
            textViewDescription = (TextView) itemView.findViewById(R.id.description);
            checkBox = (CheckBox) itemView.findViewById(R.id.check_box);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);
            checkBox.setOnCheckedChangeListener(this);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, EditActivity.class);
            intent.putExtra(COLUMN_ID, id);
            intent.putExtra(TITLE, title);
            intent.putExtra(DESCRIPTION, description);
            if (checked) {
                intent.putExtra(CHECKED, 1);
            } else {
                intent.putExtra(CHECKED, -1);
            }
            context.startActivity(intent);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ContentValues values = new ContentValues();
            values.put(FeedsTable.COLUMN_TITLE, title);
            values.put(FeedsTable.COLUMN_DESCRIPTION, description);
            values.put(FeedsTable.COLUMN_RANGE, range);
            if (isChecked) {
                values.put(FeedsTable.COLUMN_STATUS, 1);
            } else {
                values.put(FeedsTable.COLUMN_STATUS, -1);
            }

            context.getContentResolver().update(ContentUris.withAppendedId(MainActivity.ENTRIES_URI, id), values,
                    FeedsTable._ID + "=?", new String[]{String.valueOf(id)});
        }
    }

}
