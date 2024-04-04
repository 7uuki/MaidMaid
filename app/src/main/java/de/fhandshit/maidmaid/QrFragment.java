package de.fhandshit.maidmaid;

import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import de.fhandshit.maidmaid.data.model.Product;
import de.fhandshit.maidmaid.databinding.FragmentQrBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QrFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QrFragment extends Fragment {

    private FragmentQrBinding binding;
    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    //This class provides methods to play DTMF tones
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;

    public QrFragment() {
        // Required empty public constructor
    }

    public static QrFragment newInstance(String param1, String param2) {
        QrFragment fragment = new QrFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC,     100);
        surfaceView = binding.surfaceView;
        barcodeText = binding.barcodeText;
        initialiseDetectorsAndSources();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQrBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    private void initialiseDetectorsAndSources() {

        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new
                                String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {

                    barcodeText.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                barcodeText.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).email.address;
                                barcodeText.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                            } else {
                                barcodeData = barcodes.valueAt(0).displayValue;
                                barcodeText.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);

                                //Übergabe an Third Fragment

                                if (App.getRepo().getProductDao().existProduct(barcodeText.getText())) {
                                    //Produkt existiert in Datenbank mit einer QR Id
                                    Product product = App.getRepo().getProductDao().getProduct(barcodeText.getText());
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("id", product.getId());
                                    bundle.putBoolean("fromProduct", true);
                                    NavHostFragment.findNavController(QrFragment).navigate(R.id.action_QrFragment_to_ThirdFragment, bundle);
                                }else {
                                    //Produkt existiert nicht in Datenbank
                                    Product product = App.getRepo().getProductDao().getProduct(barcodeText.getText());
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("id", product.getId());
                                    bundle.putBoolean("fromProduct", false);
                                    NavHostFragment.findNavController(QrFragment).navigate(R.id.action_QrFragment_to_ThirdFragment, bundle);
                                }

                            }
                        }
                    });

                }
            }
        });
    }
}