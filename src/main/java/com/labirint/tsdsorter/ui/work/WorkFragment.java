package com.labirint.tsdsorter.ui.work;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import ru.labirint.core.entities.Barcode;
import ru.labirint.core.util.messages.Msg;
import com.labirint.tsdsorter.R;
import com.labirint.tsdsorter.databinding.WorkFragmentBinding;
import com.labirint.tsdsorter.entities.CMD;
import com.labirint.tsdsorter.ui.base.BaseFragment;
import com.labirint.tsdsorter.ui.root.RootActivity;

import static com.labirint.tsdsorter.interactors.scankeys.Scankeys.BAGE;

public class WorkFragment extends BaseFragment /*implements Interactor.MainView, ScannerListener*/ {


    //WorkViewModel viewModel;
    WorkFragmentBinding binding;


    //----------------------------------------------------

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding =  WorkFragmentBinding.inflate(inflater);

        viewModel = new ViewModelProvider(this, new WorkViewModelFactory(
                app().getQueryHelper()
                ,app().getValuesRepository()
        ))
        .get(WorkViewModel.class);
        binding.setModel(viewModel());

        // --- debug
        // дебажные кнопки
//            binding.btn1.setOnClickListener(v -> {
//                viewModel().scanUseCase.setScanKeys(BAGE);
//                viewModel().valuesRepository.setIdPerson(1);
//                viewModel().msg.say("Сканируй команду");
//                viewModel().title.setValue("Выбор команды");
//                viewModel().backResource.set(R.drawable.command);
//            });
//        binding.btn2.setOnClickListener(v -> {
//            onScan(new Barcode(CMD.ARRANGE));
//        });
//        binding.btn3.setOnClickListener(v -> {
//            onScan(new Barcode("950000001000"));
//            //onScan(new Barcode(CMD.FINISH));
//        });

        // ---



        return binding.getRoot();
    }

    WorkViewModel viewModel(){
        return  (WorkViewModel)viewModel;
    }

    @Override
    public void initViewModel() {
        viewModel().getBeep().observe(getViewLifecycleOwner(),
                beepType -> { Msg.Beep(getActivity(), beepType); });
        viewModel().getTitle().observe(getViewLifecycleOwner(), title ->  ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title));
        viewModel().onSay().observe(getViewLifecycleOwner(), b -> ((RootActivity)getActivity()).scanOn());
    }

    //------------------------------------------------------------------------------------------


    public void onScan(Barcode barcode) {
        viewModel().onScan(barcode);
    }


    // -----------------------------------------------------------------------------------------

    @Override
    public void connected() {
        viewModel().isConnected.set(true);
    }


    @Override
    public void disconnected() {
        viewModel().isConnected.set(false);
    }

    public void backScanKey() {
        viewModel().backScanKey();
    }

    // -----------------------------------------------------------------------------------------

}
