package com.example.jps;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import android.os.AsyncTask;
import android.widget.CheckBox;
import android.database.Cursor;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.Button;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


public class BasicJob extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JobAdapter jobAdapter;
    private DatabaseHelper dbHelper;
    private CheckBox someCheckBox1;
    private CheckBox someCheckBox2;
    private CheckBox someCheckBox3;
    private CheckBox someCheckBox4;
    private CheckBox someCheckBox5;
    private CheckBox someCheckBox6;
    private CheckBox someCheckBox7;
    private CheckBox someCheckBox8;
    private CheckBox someCheckBox9;
    private CheckBox someCheckBox10;
    private CheckBox someCheckBox11;
    private CheckBox someCheckBox12;
    private CheckBox someCheckBox13;
    private CheckBox someCheckBox14;
    private CheckBox someCheckBox15;
    private Spinner someSpinner;    // Spinner 예시.
    private EditText someEditText;  // EditText 예시.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_job);
        SlidingUpPanelLayout slidingLayout = findViewById(R.id.sliding_layout);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(BasicJob.this);
        Button resetButton = findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetUIElements();
                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        Button searchButton = findViewById(R.id.confirm_button);
        searchButton.setOnClickListener(v -> {
            boolean isPartTimeChecked = someCheckBox1.isChecked();
            boolean isContractChecked = someCheckBox3.isChecked();
            boolean isPermanentChecked = someCheckBox2.isChecked();
            boolean gyung1 = someCheckBox4.isChecked();
            boolean gyung2 = someCheckBox5.isChecked();
            boolean gyung3 = someCheckBox6.isChecked();
            boolean gyung4 = someCheckBox7.isChecked();
            boolean gyung5 = someCheckBox8.isChecked();
            boolean gyung6 = someCheckBox9.isChecked();
            boolean school1 = someCheckBox10.isChecked();
            boolean school2 = someCheckBox11.isChecked();
            boolean school3 = someCheckBox12.isChecked();
            boolean school4 = someCheckBox13.isChecked();
            boolean access1 = someCheckBox14.isChecked();
            boolean access2 = someCheckBox15.isChecked();
            String city = someSpinner.getSelectedItem().toString();
            String query = someEditText.getText().toString();
            jobAdapter.filter(query,isPartTimeChecked, isContractChecked, isPermanentChecked, gyung1, gyung2, gyung3, gyung4, gyung5, gyung6, school1, school2, school3, school4, access1, access2, city);
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        });
        // Start AsyncTask to read CSV file
        new ReadCSVFileTask().execute("장애인구직정보_전처리.csv");
        someCheckBox1 = findViewById(R.id.part_time_checkbox);
        someCheckBox2 = findViewById(R.id.permanent_checkbox);
        someCheckBox3 = findViewById(R.id.contract_checkbox);
        someCheckBox4 = findViewById(R.id.gyung_checkbox1);
        someCheckBox5 = findViewById(R.id.gyung_checkbox2);
        someCheckBox6 = findViewById(R.id.gyung_checkbox3);
        someCheckBox7 = findViewById(R.id.gyung_checkbox4);
        someCheckBox8 = findViewById(R.id.gyung_checkbox5);
        someCheckBox9 = findViewById(R.id.gyung_checkbox6);
        someCheckBox10 = findViewById(R.id.school_checkbox1);
        someCheckBox11 = findViewById(R.id.school_checkbox2);
        someCheckBox12 = findViewById(R.id.school_checkbox3);
        someCheckBox13 = findViewById(R.id.school_checkbox4);
        someCheckBox14 = findViewById(R.id.barrier_free_checkbox1);
        someCheckBox15 = findViewById(R.id.barrier_free_checkbox2);
        someSpinner = findViewById(R.id.region_spinner);
        someEditText = findViewById(R.id.company_name_input);
    }
    private void resetUIElements() {
        // CheckBox 초기화
        someCheckBox1.setChecked(false);
        someCheckBox2.setChecked(false);
        someCheckBox3.setChecked(false);
        someCheckBox4.setChecked(false);
        someCheckBox5.setChecked(false);
        someCheckBox6.setChecked(false);
        someCheckBox7.setChecked(false);
        someCheckBox8.setChecked(false);
        someCheckBox9.setChecked(false);
        someCheckBox10.setChecked(false);
        someCheckBox11.setChecked(false);
        someCheckBox12.setChecked(false);
        someCheckBox13.setChecked(false);
        someCheckBox14.setChecked(false);
        someCheckBox15.setChecked(false);
        // Spinner 초기화
        someSpinner.setSelection(0);     // 첫 번째 아이템 선택

        // EditText 초기화
        someEditText.setText("");        // 텍스트 지우기
        Cursor cursor = dbHelper.getAllJobs(); // 데이터베이스에서 모든 데이터 다시 가져오기
        jobAdapter.reloadData(cursor);
    }

    private class ReadCSVFileTask extends AsyncTask<String, Void, Void> {
        List<String[]> allData = new ArrayList<>();
        @Override
        protected Void doInBackground(String... filenames) {

            try {
                InputStreamReader inputStreamReader = new InputStreamReader(getAssets().open(filenames[0]), "UTF-8");
                CSVReader csvReader = new CSVReader(inputStreamReader);

                // 헤더 행 스킵
                csvReader.readNext();

                String[] nextLine;
                while ((nextLine = csvReader.readNext()) != null) {
                    allData.add(nextLine);
                }
                csvReader.close();
            } catch (IOException | CsvValidationException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dbHelper.addJob(allData); // 데이터베이스에 데이터 추가
            Cursor cursor = dbHelper.getAllJobs();
            jobAdapter = new JobAdapter(cursor);
            recyclerView.setAdapter(jobAdapter);
        }
    }

    class JobViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3, textView4, textView5, textView6;
        CheckBox checkBox;
        JobViewHolder(View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            textView5 = itemView.findViewById(R.id.textView5);
            textView6 = itemView.findViewById(R.id.textView6);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
    public static class JobData {
        private String[] data;
        private boolean isChecked;

        // static 변수로 체크된 아이템들을 저장할 리스트 추가
        public static List<JobData> checkedItems = new ArrayList<>();

        public JobData(String[] data) {
            this.data = data;
        }

        public String[] getData() {
            return data;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            if(checked && !this.isChecked) {
                // 아이템을 체크 리스트에 추가
                checkedItems.add(this);
            } else if (!checked && this.isChecked) {
                // 아이템을 체크 리스트에서 삭제
                checkedItems.remove(this);
            }
            this.isChecked = checked;
        }

        // static 메서드로 체크된 아이템 리스트를 얻는 메서드 추가
        public static List<JobData> getCheckedItems() {
            return checkedItems;
        }
    }

    class JobAdapter extends RecyclerView.Adapter<JobViewHolder> {
        private List<JobData> jobDataList;

        JobAdapter(Cursor cursor) {
            this.jobDataList = new ArrayList<>();
            while(cursor.moveToNext()) {
                String companyName = getColumnValue(cursor, "company_name");
                String jobType = getColumnValue(cursor, "job_type");
                String company_contract = getColumnValue(cursor, "contract");
                String company_address = getColumnValue(cursor, "Address");
                String company_StartDay = getColumnValue(cursor, "Start_Day");
                String company_EndDay = getColumnValue(cursor, "End_Day");
                String company_Gyung = getColumnValue(cursor, "Gyung");
                String company_Hak = getColumnValue(cursor, "Hak");
                String company_Access = getColumnValue(cursor, "Access");
                String company_City = getColumnValue(cursor, "City");

                if(companyName != null && jobType != null && company_contract != null && company_address != null && company_StartDay != null && company_EndDay != null && company_Gyung != null && company_Hak != null && company_Access != null && company_City != null) {
                    String[] data = new String[] { companyName, jobType, company_contract, company_address, company_StartDay, company_EndDay , company_Gyung, company_Hak, company_Access, company_City};
                    this.jobDataList.add(new JobData(data));
                }
            }
        }
        public void reloadData(Cursor cursor) {
            this.jobDataList.clear(); // 기존 리스트 데이터를 클리어합니다.

            while(cursor.moveToNext()) {
                String companyName = getColumnValue(cursor, "company_name");
                String jobType = getColumnValue(cursor, "job_type");
                String company_contract = getColumnValue(cursor, "contract");
                String company_address = getColumnValue(cursor, "Address");
                String company_StartDay = getColumnValue(cursor, "Start_Day");
                String company_EndDay = getColumnValue(cursor, "End_Day");
                String company_Gyung = getColumnValue(cursor, "Gyung");
                String company_Hak = getColumnValue(cursor, "Hak");
                String company_Access = getColumnValue(cursor, "Access");
                String company_City = getColumnValue(cursor, "City");

                if(companyName != null && jobType != null && company_contract != null && company_address != null && company_StartDay != null && company_EndDay != null && company_Gyung != null && company_Hak != null && company_Access != null && company_City != null) {
                    String[] data = new String[] { companyName, jobType, company_contract, company_address, company_StartDay, company_EndDay , company_Gyung, company_Hak, company_Access, company_City};
                    this.jobDataList.add(new JobData(data));
                }
            }

            notifyDataSetChanged(); // 데이터 변경 알림
        }

        private String getColumnValue(Cursor cursor, String columnName) {
            int index = cursor.getColumnIndex(columnName);
            if(index != -1) {
                return cursor.getString(index);
            }
            return null;
        }
        public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
            return new JobViewHolder(view);
        }

        public void onBindViewHolder(JobViewHolder holder, int position) {
            JobData job = jobDataList.get(position);

            holder.textView1.setText("회사명 : " + job.getData()[0]);
            holder.textView2.setText("직종 업무 : " + job.getData()[1]);
            holder.textView3.setText("계약 구분 : " + job.getData()[2]);
            holder.textView4.setText("주소 : " + job.getData()[3]);
            holder.textView5.setText("시작일자 : " + job.getData()[4]);
            holder.textView6.setText("마감일자 : " + job.getData()[5]);
            // 1. 기존 리스너 제거
            holder.checkBox.setOnCheckedChangeListener(null);

            // 2. 체크박스 상태 설정
            holder.checkBox.setChecked(job.isChecked());

            // 3. 새 리스너 설정
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> job.setChecked(isChecked));
        }

        public int getItemCount() {
            return jobDataList.size();
        }
        public void filter(String query, boolean isPartTimeChecked, boolean isContractChecked, boolean isPermanentChecked, boolean gyung1, boolean gyung2, boolean gyung3, boolean gyung4, boolean gyung5, boolean gyung6, boolean school1, boolean school2, boolean school3, boolean school4, boolean access1, boolean access2, String city) {
            List<JobData> filteredList = new ArrayList<>();
            Cursor cursor = dbHelper.getAllJobs(); // 데이터베이스에서 모든 데이터 다시 가져오기
            jobAdapter.reloadData(cursor);
            for (JobData job : jobDataList) {
                String companyName = job.getData()[0].toLowerCase();
                String employmentType = job.getData()[2].toLowerCase();
                String employmentGyung = job.getData()[6].toLowerCase();
                String employmentSchool = job.getData()[7].toLowerCase();
                String employmentaccess = job.getData()[8].toLowerCase();
                String employmentCity = job.getData()[9].toLowerCase();
                // 쿼리가 비어 있거나, 해당 쿼리를 포함하면 true
                boolean matchesQuery = query.isEmpty() || companyName.contains(query.toLowerCase());

                // 체크박스 조건에 맞으면 true
                boolean isMatchedByCheckbox =
                        (isPartTimeChecked && employmentType.equals("시간제")) ||
                                (isContractChecked && employmentType.equals("계약직")) ||
                                (isPermanentChecked && employmentType.equals("상용직"));

                // 체크박스를 하나도 선택하지 않았을 경우, 체크박스 조건은 무시
                boolean noCheckboxChecked = !(isPartTimeChecked || isContractChecked || isPermanentChecked);

                boolean isMatchedGyungCheckbox =
                        (gyung1 && (employmentGyung.equals("1년미만") || employmentGyung.equals("무관"))) ||
                        (gyung2 && employmentGyung.equals("1년")) ||
                        (gyung3 && employmentGyung.equals("2년")) || (gyung4 && employmentGyung.equals("3년")) || (gyung5 && employmentGyung.equals("4년"))
                        || (gyung6 && employmentGyung.equals("5년이상"));

                boolean noCheckboxGyungChecked = !(gyung1 || gyung2 || gyung3 || gyung4 || gyung5 || gyung6);

                boolean isMatchedSchoolCheckbox =
                        (school1 && (employmentSchool.equals("무관"))) ||
                                (school2 && employmentSchool.equals("고졸")) ||
                                (school3 && employmentSchool.equals("초대졸")) ||
                                (school4 && employmentSchool.equals("대졸"));

                boolean noCheckboxSchoolChecked = !(school1 || school2 || school3 || school4);

                boolean isMatchedAccessCheckbox =
                        (access1 && (employmentaccess.equals("있음"))) ||
                                (access2 && (employmentaccess.equals("없음")));

                boolean noCheckboxAccessChecked = !(access1 || access2);
                boolean matchesCity = city.isEmpty() || employmentCity.contains(city.toLowerCase());
                // 쿼리와 체크박스 조건 둘 다 만족하거나, 체크박스가 선택되지 않았을 때만 쿼리 조건을 확인
                if (matchesQuery && (isMatchedByCheckbox || noCheckboxChecked) && (isMatchedGyungCheckbox || noCheckboxGyungChecked) && (isMatchedSchoolCheckbox || noCheckboxSchoolChecked) && (isMatchedAccessCheckbox || noCheckboxAccessChecked) && matchesCity) {
                    filteredList.add(job);
                }
            }
            jobDataList = filteredList;
            notifyDataSetChanged();
        }
    }
}
