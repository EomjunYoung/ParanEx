package kr.ac.ajou.paran.util.dialog;

import android.content.Context;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.stage.main.function.timeTable.sub.dialog.SubjectInput;
import kr.ac.ajou.paran.util.DialogType;
import kr.ac.ajou.paran.util.HTTP;
import kr.ac.ajou.paran.util.adapter.LectureAdapter;

/**
 * Created by dream on 2017-12-01.
 */

public class AddSubject extends DialogType {
    private EditText editText;
    private SubjectInput subjectInput;

    private ListView listSubject;
    private ArrayList<String> subjects;
    private LectureAdapter subjectAdapter;

    private final String majors[][] = {{"전공"},
            {"전공","기계공학전공(과)","산업공학전공(과)","산업정보시스템공학전공(과)","화학공학전공(과)","신소재공학전공(과)","응용화학생명공학전공(과)","환경안전공학전공(과)","건설시스템공학전공(과)","교통시스템공학전공(과)","건축학전공(5년)(과)","건축공학전공(과)", "전자공학전공(과)", "소프트웨어및컴퓨터공학전공(과)", "사이버보안전공(과)", "미디어콘텐츠전공(과)", "소셜미디어전공(과)", "공군ICT전공(과)", "ICT융합전공(과)", "수학전공(과)", "물리학전공(과)", "화학전공(과)", "생명과학전공(과)", "경영학전공(과)", "e-비즈니스학전공(과)", "금융공학전공(과)", "스포츠마케팅학전공(과)", "국어국문학전공(과)", "영어영문학전공(과)", "불어불문학전공(과)", "사학전공(과)", "문화콘텐츠학전공(과)", "경제학전공(과)", "행정학전공(과)", "심리학전공(과)", "사회학전공(과)", "정치외교학전공(과)", "스포츠레저학전공(과)", "법학전공(과)", "의학전공(과)", "간호학전공(과)", "간호학전공(특별과정)(과)", "약학전공(과)", "한국학전공(과)", "국제통상전공(과)", "문화산업과커뮤니케이션전공(과)", "중국지역연구전공(과)", "일본지역연구전공(과)", "인문사회데이터분석전공(과)", "융합시스템공학전공(과)"},
            {"구분","전체"},
            {"학부", "공통", "기계공학과", "산업공학과", "화학공학과", "신소재공학과", "응용화학생명공학과", "환경안전공학과", "건설시스템공학과", "교통시스템공학과", "건축학과", "전자공학과", "소프트웨어학과", "미디어학과", "국방디지털융합학과", "사이버보안학과", "수학과", "물리학과", "화학과", "생명과학과", "경영학과", "e-비즈니스학과", "금융공학과", "국어국문학과", "사회과학대학(과)", "영어영문학과", "불어불문학과", "사학과", "문화콘텐츠학과", "경제학과", "행정학과", "심리학과", "사회학과", "정치외교학과", "스포츠레저학과", "법학과", "의학과", "간호학과", "간호학과(특별과정)", "자유전공", "약학과", "문화산업과 커뮤니케이션전공", "인문사회데이터분석전공", "기초의과학전공", "융합시스템공학과", "자동차SW전공", "디지털휴머니티전공"},
            {"영역", "전체", "사고와표현", "역사와가치관", "문화와예술", "인간과사회", "과학과기술", "역사와철학(인문학1)", "문학과예술(인문학2)", "인간과사회(사회과학)", "자연과과학(자연과학)"}};

    private final String codes[][] = {{""},
            {"","DS03001021001", "DS03001022001", "DS03001022002", "DS03001023001", "DS03001024001", "DS03001025001", "DS03001026001", "DS03001027001", "DS03001028001", "DS03001029003", "DS03001029001", "DS03002021001", "DS030020201", "DS030020101", "DS03002024001", "DS03002024003", "DS03002025001", "DS030020202", "DS03003021001", "DS03003022001", "DS03003023001", "DS03003024001", "DS03004021001", "DS03004022001", "DS03004023001", "DS03004021002", "DS03005021001", "DS03005022001", "DS03005023001", "DS03005024001", "DS03005025001", "DS03006021001", "DS03006022001", "DS03006023001", "DS03006024001", "DS03006025001", "DS03006026001","DS03007021001", "DS03008002001", "DS03009001001", "DS03009002001", "DS03013021001", "DS03012001006", "DS03012001001", "DS03006027001", "DS03012001002","DS03012001003", "DS030020301", "DS03001030001"},
            {"",""},
            {"","00", "DS03001021", "DS03001022", "DS03001023", "DS03001024", "DS03001025", "DS03001026", "DS03001027", "DS03001028", "DS03001029", "DS03002021", "DS0300202", "DS03002024", "DS03002025", "DS0300201", "DS03003021", "DS03003022", "DS03003023", "DS03003024", "DS03004021", "DS03004022", "DS03004023", "DS03005021", "DS03006029", "DS03005022", "DS03005023", "DS03005024", "DS03005025", "DS03006021", "DS03006022", "DS03006023", "DS03006024", "DS03006025", "DS03006026", "DS03007021", "DS03008002", "DS03009001", "DS03009002", "DS03010002", "DS03013021", "DS03006027", "DS0300203", "DS03003025", "DS03001030", "DS0300204", "DS03005026"},
            {"","", "U0204001", "U0204002", "U0204003", "U0204004", "U0204005", "U0204006", "U0204007", "U0204008", "U0204009"}};

    public AddSubject(Context context, SubjectInput subjectInput) {
        super(context, R.layout.dialog_add_subject);
        this.subjectInput = subjectInput;

        initSetting(context);
    }

    public AddSubject(Context context, EditText editText) {
        super(context, R.layout.dialog_add_subject);
        this.editText = editText;

        initSetting(context);
    }

    private void initSetting(Context context) {
        setNetwork();
        setButton();
        setList();
        setSpinner(context);
    }

    private void setNetwork() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void setSpinner(final Context context) {
        Spinner spinnerType = (Spinner) findViewById(R.id.spinnerType);
        final Spinner spinnerMajor = (Spinner) findViewById(R.id.spinnerMajor);

        String types[] ={"학수 구분","전공과목","교양과목","기초과목","영역별교양"};
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_activated_1, types);
        spinnerType.setAdapter(adapterType);

        final ArrayAdapter<String> adapterMajor[] = new ArrayAdapter[5];
        for(int i=0;i<5;i++)
            adapterMajor[i] = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_activated_1, majors[i]);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                spinnerMajor.setAdapter(adapterMajor[i]);
                spinnerMajor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int j, long l) {
                        if (i != 0 && j != 0) {
                            subjects = HTTP.printLecture(i, codes[i][j]);
                            subjectAdapter = new LectureAdapter(context, R.layout.list_subject, subjects);
                            listSubject.setAdapter(subjectAdapter);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setList() {
        listSubject = (ListView)findViewById(R.id.listSubject);
        listSubject.setAdapter(subjectAdapter);
    }

    private void setButton() {
        Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
        Button buttonOK = (Button) findViewById(R.id.buttonOK);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subjectAdapter != null) {
                    int cursor = subjectAdapter.getCursor();
                    if (cursor > -1) {
                        if (editText != null)
                            editText.setText(subjects.get(subjectAdapter.getCursor()).toString());
                        else if (subjectInput != null)
                            subjectInput.addSubject(subjects.get(subjectAdapter.getCursor()).toString());
                        dismiss();
                    }
                }
            }
        });
    }
}
