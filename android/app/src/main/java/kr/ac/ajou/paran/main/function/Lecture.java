package kr.ac.ajou.paran.main.function;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.StringTokenizer;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.util.FunctionType;


/**
 * Created by user on 2017-08-11.
 */

public class Lecture extends FunctionType implements Callback {


    private Spinner sptest;
    private Spinner spyear, spsemester, sptype, spmajor;
    private Button testbtn;
    private Callback mCallback;
    private String year;
    private String semester;
    private String type;
    private String major;
    private static LinkedHashMap<String, String> majorMap;
    private static LinkedHashMap<String, String> basicMap;
    private static LinkedHashMap<String, String> semesterMap;
    private static LinkedHashMap<String, String> typeMap;



    String[] yearStrings = {"2016", "2017"};
    String[] semesterStrings = {"1학기", "여름학기", "2학기", "겨울학기"};
    String[] typeStrings = {"전공과목", "교양과목", "기초과목", "공학기초", "영역별교양", "학점교류", "공학인증교양"};
    String[] majorStrings = {"기계공학전공", "기계공학전공(과)", "산업정보시스템공학전공", "산업공학전공(과)", "산업정보시스템공학전공(과)" +
    "화학공학전공", "환경공학전공(과)", "신소재공학전공", "신소재공학전공(과),", "응용화학전공", "생명공학전공", "응용화학생명공학전공" +
   "응용화학생명공학전공(과)", "환경공학전공", "환경안전공학전공(과)", "건설시스템공학전공", "건설시스템공학전공(과)" +
    "교통시스템공학전공", "교통시스템공학전공(과)", "건축학전공", "건축학전공(과)", "건축학전공(5년)", "건축학전공(5년)(과)"+
    "건축공학전공", "건축공학전공(과)", "전자공학전공", "전자공학전공(과)", "소프트웨어및컴퓨터공학전공(과)", "사이버보안전공(과)"+
    "정보컴퓨터공학전공", "컴퓨터공학전공(과)", "소프트웨어보안전공(과)", "정보컴퓨터공학전공(과)", "소프트웨어융합전공" +
    "미디어학전공", "미디어콘텐츠전공(과)", "미디어학전공(과)", "소셜미디어전공(과)", "공군ICT전공(과)", "ICT융합전공(과)" +
    "수학전공", "수학전공(과)", "물리학전공", "물리학전공(과)", "화학전공", "화학전공(과)", "생명과학전공", "생명과학전공(과)"+
    "경영학전공", "경영학전공(과)", "e-비즈니스학전공", "e-비즈니스학전공(과)", "금융공학전공", "금융공학전공(과)" +
    "스포츠마케팅학전공", "스포츠마케팅학전공(과)", "국어국문학전공", "국어국문학전공(과)", "영어영문학전공", "영어영문학전공(과)" +
    "불어불문학전공", "불어불문학전공(과)", "사학전공", "사학전공(과)", "문화콘텐츠학전공", "경제학전공", "경제학전공(과)", "행정학전공" +
    "행정학전공(과)", "심리학전공", "심리학전공(과)", "정치외교학전공", "정치외교학전공(과)", "스포츠레저학전공", "스포츠레저학전공(과)"+
    "법학전공", "법학전공(과)", "의학전공(과)", "간호학전공(과)", "간호학전공(특별과정)(과)", "자유전공", "약학전공(과)"+
    "약학전공", "한국학전공(과)", "기초의과학전공", "국제통상전공(과)", "문화산업과커뮤니케이션전공", "문화산업과커뮤니케이션전공(과)" +
    "중국지역연구전공(과)", "문화학전공", "일본지역연구전공(과)", "지역연구전공(유럽)(과)", "지연연구전공(미국)(과)" +
    "인문사회데이터분석전공(과)", "응용화학전공(과)", "생명공학전공(과)", "소프트웨어융합전공(과)", "기초의과학전공(과)"+
    "융합시스템공학전공(과)", "ICT융합전공(과)(폐지)", "자동차SW전공(과)", "디지털휴머니티전공(과)"};

    String[] basicStrings = {"기계공학부", "기계공학과", "산업정보시스템공학부", "산업공학과", "화공ㆍ신소재공학부"};



    public Lecture() {
        super("강의시간표", R.layout.activity_lecture);


    }

    @Override
    protected void onCreate(Bundle savedlnstanceState) {
        super.onCreate(savedlnstanceState);

        exThread exthread = new exThread();


        mCallback = this;
        exthread.initspinner();
        init();


    }

    private class exThread extends Thread {

        public exThread() {

        }

        public void initspinner() {

            spyear = (Spinner) findViewById(R.id.year);
            spsemester = (Spinner) findViewById(R.id.semester);
            sptype = (Spinner) findViewById(R.id.type);
            spmajor = (Spinner) findViewById(R.id.major);
            majorMap = setHashMap(majorMap);
            basicMap = setHashMap1(basicMap);
            semesterMap = setHashMap2(semesterMap);
            typeMap = setHashMap3(typeMap);


            final ArrayAdapter arrayAdapter1 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, yearStrings);
            final ArrayAdapter arrayAdapter2 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, semesterStrings);
            final ArrayAdapter arrayAdapter3 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, typeStrings);
            final ArrayAdapter arrayAdapter4 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, majorStrings);
            final ArrayAdapter arrayAdapter5 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, basicStrings);


            spyear.setAdapter(arrayAdapter1);
            spsemester.setAdapter(arrayAdapter2);
            sptype.setAdapter(arrayAdapter3);
            spmajor.setAdapter(arrayAdapter4);


            spyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if (spyear.getSelectedItem().toString() == "2017")
                        year = "2017";

                    else if (spyear.getSelectedItem().toString() == "2016")
                        year = "2016";
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            spsemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    Set<String> set = semesterMap.keySet();
                    Iterator<String> itr = set.iterator();


                    while(itr.hasNext())
                    {
                        String str = itr.next();

                        if(spsemester.getSelectedItem().toString() == str)
                        {
                            semester = semesterMap.get(str);
                        }
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            sptype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                    Set<String> set = typeMap.keySet();
                    Iterator<String> itr = set.iterator();

                    while(itr.hasNext())
                    {
                        String str = itr.next();
                        if(sptype.getSelectedItem().toString() == str)
                                {
                                    type = typeMap.get(str);

                                    if (type == "U0209003")
                                    {

                                        arrayAdapter5.notifyDataSetChanged();
                                        spmajor.setAdapter(arrayAdapter5);

                            }

                            else if(type == "U0209001")
                            {

                                arrayAdapter4.notifyDataSetChanged();
                                spmajor.setAdapter(arrayAdapter4);
                            }

                        }
                    }



                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });



            spmajor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {





    @Override
    public void onItemSelected (AdapterView < ? > adapterView, View view,int i, long l){

        if(type == "U0209001") {
            Set<String> set = majorMap.keySet();
            Iterator<String> itr = set.iterator();

            while (itr.hasNext()) {

                String str = itr.next();



                if (spmajor.getSelectedItem().toString() == str) {

                    major = majorMap.get(str);

                }
            }
        }
        else if(type == "U0209003")
        {
           Set<String> set = basicMap.keySet();
            Iterator<String> itr = set.iterator();

            while (itr.hasNext()) {

                String str = itr.next();



                if (spmajor.getSelectedItem().toString() == str) {

                    major = basicMap.get(str);

                }
            }
        }


    }
              @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

    }

    public void init() {
        sptest = (Spinner) findViewById(R.id.sptest);
        testbtn = (Button) findViewById(R.id.testbtn);


        testbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetworkAsync networkAsync = new NetworkAsync(semester, type, major, mCallback);
                networkAsync.execute();
            }
        });

    }


    @Override
    public void getReturn(Object o) {


        String string = o.toString();
        StringTokenizer s = new StringTokenizer(string);
        ArrayList arrayList = new ArrayList();

        while (s.hasMoreTokens()) {

            arrayList.add(s.nextToken());
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
        sptest.setAdapter(arrayAdapter);


    }

    public static LinkedHashMap<String, String> setHashMap(LinkedHashMap<String, String> majorMap)
    {
        majorMap = new LinkedHashMap<>();
        majorMap.put("기계공학전공", "DS03001002001");
        majorMap.put("기계공학전공(과)", "DS03001021001");
        majorMap.put("산업정보시스템공학전공", "DS03001003001");
        majorMap.put("산업공학전공(과)", "DS03001022001");
        majorMap.put("산업정보시스템공학전공(과)", "DS03001022002");
        majorMap.put("화학공학전공", "DS03001004002");
        majorMap.put("화학공학전공(과)", "DS03001023001");
        majorMap.put("신소재공학전공", "DS03001004001");
        majorMap.put("신소재공학전공(과)", "DS03001024001");
        majorMap.put("응용화학전공", "DS03001005001");
        majorMap.put("생명공학전공", "DS03001005002");
        majorMap.put("응용화학생명공학전공", "DS03001006001");
        majorMap.put("응용화학생명공학전공(과)", "DS03001025001");
        majorMap.put("환경공학전공", "DS03001007001");
        majorMap.put("환경안전공학전공(과)", "DS03001026001");
        majorMap.put("건설시스템공학전공", "DS03001007002");
        majorMap.put("건설시스템공학전공(과)", "DS03001027001");
        majorMap.put("교통시스템공학전공", "DS03001007003");
        majorMap.put("교통시스템공학전공(과)", "DS03001028001");
        majorMap.put("건축학전공", "DS03001008001");
        majorMap.put("건축학전공(과)", "DS03001029002");
        majorMap.put("건축학전공(5년)", "DS03001008002");
        majorMap.put("건축학전공(5년)(과)", "DS03001029003");
        majorMap.put("건축공학전공", "DS03001008003");
        majorMap.put("건축공학전공(과)", "DS03001029001");
        majorMap.put("전자공학전공", "DS03002002001");
        majorMap.put("전자공학전공(과)", "DS03002021001");
        majorMap.put("소프트웨어및컴퓨터공학전공(과)", "DS030020201");
        majorMap.put("사이버보안전공(과)", "DS030020101");
        majorMap.put("정보컴퓨터공학전공", "DS03002003001");
        majorMap.put("컴퓨터공학전공(과)", "DS03002022001");
        majorMap.put("소프트웨어보안전공(과)", "DS03002022002");
        majorMap.put("정보컴퓨터공학전공(과)", "DS03002022003");
        majorMap.put("소프트웨어융합전공", "DS03002003002");
        majorMap.put("미디어학전공", "DS03002004001");
        majorMap.put("미디어콘텐츠전공(과)", "DS03002024001");
        majorMap.put("미디어학전공(과)", "DS03002024002");
        majorMap.put("소셜미디어전공(과)", "DS03002024003");
        majorMap.put("공군ICT전공(과)", "DS03002025001");
        majorMap.put("ICT융합전공(과)", "DS030020202");
        majorMap.put("수학전공", "DS03003002001");
        majorMap.put("수학전공(과)", "DS03003021001");
        majorMap.put("물리학전공", "DS03003002002");
        majorMap.put("물리학전공(과)", "DS03003022001");
        majorMap.put("화학전공", "DS03003002003");
        majorMap.put("화학전공(과)", "DS03003023001");
        majorMap.put("생명과학전공", "DS03003002004");
        majorMap.put("생명과학전공(과)", "DS03003024001");
        majorMap.put("경영학전공", "DS03004002001");
        majorMap.put("경영학전공(과)", "DS03004021001");
        majorMap.put("e-비즈니스학전공", "DS03004003001");
        majorMap.put("e-비즈니스학전공(과)", "DS03004022001");
        majorMap.put("금융공학전공", "DS0300400501");
        majorMap.put("금융공학전공(과)", "DS03004023001");
        majorMap.put("스포츠마케팅학전공", "DS03004004001");
        majorMap.put("스포츠마케팅학전공(과)", "DS03004021002");
        majorMap.put("국어국문학전공", "DS03005002001");
        majorMap.put("국어국문학전공(과)", "DS03005021001");
        majorMap.put("영어영문학전공", "DS03005002002");
        majorMap.put("영어영문학전공(과)", "DS03005022001");
        majorMap.put("불어불문학전공", "DS03005002003");
        majorMap.put("불어불문학전공(과)", "DS03005023001");
        majorMap.put("사학전공", "DS03005002004");
        majorMap.put("사학전공(과)", "DS03005024001");
        majorMap.put("문화콘텐츠학전공", "DS03005002005");
        majorMap.put("문화콘텐츠학전공(과)", "DS03005025001");
        majorMap.put("경제학전공", "DS03006002001");
        majorMap.put("경제학전공(과)", "DS03006021001");
        majorMap.put("행정학전공", "DS03006002002");
        majorMap.put("행정학전공(과)", "DS03006022001");
        majorMap.put("심리학전공", "DS03006002003");
        majorMap.put("심리학전공(과)", "DS03006023001");
        majorMap.put("사회학전공", "DS03006002004");
        majorMap.put("사회학전공(과)", "DS03006024001");
        majorMap.put("정치외교학전공", "DS03006002005");
        majorMap.put("정치외교학전공(과)", "DS03006025001");
        majorMap.put("스포츠레저학전공", "DS03006003001");
        majorMap.put("스포츠레저학전공(과)", "DS03006026001");
        majorMap.put("법학전공", "DS03007002001");
        majorMap.put("법학전공(과)", "DS03007021001");
        majorMap.put("의학전공(과)", "DS03008002001");
        majorMap.put("간호학전공(과)", "DS03009001001");
        majorMap.put("간호학전공(특별과정)(과)", "DS03009002001");
        majorMap.put("자유전공", "DS03010002001");
        majorMap.put("약학전공(과)", "DS03013021001");
        majorMap.put("약학전공", "DS03013002001");
        majorMap.put("한국학전공(과)", "DS03012001006");
        majorMap.put("기초의과학전공", "DS03003002801");
        majorMap.put("국제통상전공(과)", "DS03012001001");
        majorMap.put("문화산업과커뮤니케이션전공", "DS03006002801");
        majorMap.put("문화산업과커뮤니케이션전공(과)", "DS03006027001");
        majorMap.put("중국지역연구전공(과)", "DS03012001002");
        majorMap.put("문화학전공", "DS03005002801");
        majorMap.put("일본지역연구전공(과)", "DS03012001003");
        majorMap.put("지역연구전공(유럽)(과)", "DS03012001004");
        majorMap.put("지역연구전공(미국)(과)", "DS03012001005");
        majorMap.put("인문사회데이터분석전공(과)", "DS030020301");
        majorMap.put("응용화학전공(과)", "DS03001025002");
        majorMap.put("생명공학전공(과)", "DS03001025003");
        majorMap.put("소프트웨어융합전공(과)", "DS03002023001");
        majorMap.put("기초의과학전공(과)", "DS03003025001");
        majorMap.put("융합시스템공학전공(과)", "DS03001030001");
        majorMap.put("ICT융합전공(과)(폐지)", "DS03002022004");
        majorMap.put("자동차SW전공(과)", "DS030020401");
        majorMap.put("디지털휴머니티전공(과)", "DS0300502601");


        return majorMap;
    }

    public static LinkedHashMap<String, String> setHashMap1(LinkedHashMap<String, String> basicMap) {
        basicMap = new LinkedHashMap<>();

        basicMap.put("기계공학부", "DS03001002");
        basicMap.put("기계공학과", "DS03001021");
        basicMap.put("산업정보시스템공학부", "DS03001003");
        basicMap.put("산업공학과", "DS03001022");
        basicMap.put("화공ㆍ신소재공학부", "DS03001004");
        basicMap.put("화학공학과", "DS03001023");
        basicMap.put("신소재공학과", "DS03001024");
        basicMap.put("생명ㆍ분자공학부", "DS03001005");
        basicMap.put("응용화학생명공학부", "DS03001006");
        basicMap.put("응용화학생명공학과", "DS03001025");
        basicMap.put("환경안전공학과", "DS03001026");
        basicMap.put("건설시스템공학과", "DS03001027");
        basicMap.put("교통시스템공학과", "DS03001028");
        basicMap.put("건축학부", "DS03001008");
        basicMap.put("건축학과", "DS03001029");
        basicMap.put("전자공학부", "DS03002002");
        basicMap.put("전자공학과", "DS03002021");
        basicMap.put("소프트웨어학과", "DS0300202");
        basicMap.put("기계공학부", "DS03001002");
        basicMap.put("기계공학부", "DS03001002");
        basicMap.put("기계공학부", "DS03001002");
        basicMap.put("기계공학부", "DS03001002");
        basicMap.put("기계공학부", "DS03001002");
        basicMap.put("기계공학부", "DS03001002");
        basicMap.put("기계공학부", "DS03001002");
        basicMap.put("기계공학부", "DS03001002");
        basicMap.put("기계공학부", "DS03001002");
        basicMap.put("기계공학부", "DS03001002");
        basicMap.put("기계공학부", "DS03001002");
        basicMap.put("기계공학부", "DS03001002");
        basicMap.put("기계공학부", "DS03001002");
        basicMap.put("기계공학부", "DS03001002");




        return basicMap;
    }

    public static LinkedHashMap<String, String> setHashMap2(LinkedHashMap<String, String> semesterMap)
    {
        semesterMap = new LinkedHashMap<>();

        semesterMap.put("1학기", "U0002001");
        semesterMap.put("여름학기","U0002002");
        semesterMap.put("2학기", "U0002003");
        semesterMap.put("겨울학기", "U0002004");


        return semesterMap;
    }


    public static LinkedHashMap<String, String> setHashMap3(LinkedHashMap<String, String> typeMap)
    {
        typeMap = new LinkedHashMap<>();

        typeMap.put("전공과목", "U0209001");
        typeMap.put("교양과목", "U0209002");
        typeMap.put("기초과목", "U0209003");
        typeMap.put("공학기초", "U0209004");
        typeMap.put("영역별교양","U0209005");
        typeMap.put("학점교류", "U0209006");
        typeMap.put("공학인증교양", "U0209007");

        return typeMap;
    }




}




