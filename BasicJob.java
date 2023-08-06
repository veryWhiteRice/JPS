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

public class BasicJob extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JobAdapter jobAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_job);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Start AsyncTask to read CSV file
        new ReadCSVFileTask().execute("장애인구직정보_전처리.csv");
    }

    private class ReadCSVFileTask extends AsyncTask<String, Void, List<String[]>> {
        @Override
        protected List<String[]> doInBackground(String... filenames) {
            List<String[]> data = new ArrayList<>();
            try {
                data = readCSVFileFromAssets(filenames[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(List<String[]> data) {
            jobAdapter = new JobAdapter(data);
            recyclerView.setAdapter(jobAdapter);
        }
    }
    private List<String[]> readCSVFileFromAssets(String fileName) throws IOException {
        List<String[]> data = new ArrayList<>();

        InputStreamReader inputStreamReader = new InputStreamReader(getAssets().open(fileName), "UTF-8");
        CSVReader csvReader = new CSVReader(inputStreamReader);

        String[] nextLine;

        // 첫 번째 행(헤더) 읽고 무시
        try {
            csvReader.readNext();
        } catch (CsvValidationException e) {
            e.printStackTrace();
        }

        // 이후 행들 읽기
        try {
            while ((nextLine = csvReader.readNext()) != null) {
                data.add(nextLine);
            }
        } catch (CsvValidationException e) {
            e.printStackTrace();
        }

        csvReader.close();

        return data;
    }
    class JobViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3, textView4, textView5, textView6;

        JobViewHolder(View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            textView5 = itemView.findViewById(R.id.textView5);
            textView6 = itemView.findViewById(R.id.textView6);
        }
    }

    class JobAdapter extends RecyclerView.Adapter<JobViewHolder> {
        private final List<String[]> data;

        JobAdapter(List<String[]> data) {
            this.data = data;
        }

        @Override
        public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
            return new JobViewHolder(view);
        }

        @Override
        public void onBindViewHolder(JobViewHolder holder, int position) {
            String[] row = data.get(position);
            holder.textView1.setText("회사명 : " + row[1]);
            holder.textView2.setText("직종 업무 : " + row[2]);
            holder.textView3.setText("계약 구분 : " + row[3]);
            holder.textView4.setText("주소 : " + row[11]);
            holder.textView5.setText("시작일자 : " + row[20]);
            holder.textView6.setText("마감일자 : " + row[21]);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
