package kr.ac.ajou.paran.stage.main.function.timeTable.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.stage.main.function.timeTable.TimeTable;
import kr.ac.ajou.paran.util.adapter.TableAdapter;
import kr.ac.ajou.paran.util.dialog.TwoWays;

/**
 * Created by dream on 2017-12-01.
 */

public class DeleteTimeTable extends TwoWays {
    private final Button buttonOK;
    private final Button buttonCancel;
    public DeleteTimeTable(final Context context) {
        super(context);
        buttonOK = super.getButtonOK();
        buttonCancel = super.getButtonNO();

        super.setContent("해당 내용을 삭제하시겠습니까?");

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            dismiss();
            }
        });
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeTable timeTable = (TimeTable)context;
                TableAdapter tableAdapter = timeTable.getTableAdapter();
                int cursor = tableAdapter.getCursor();
                ArrayList<String> subjects = timeTable.getSubjects();
                subjects.set(cursor,"");
                tableAdapter.setCursor(-1);
                tableAdapter.notifyDataSetChanged();
                dismiss();
            }
        });
    }
}
