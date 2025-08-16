package com.example.BloodCare;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BloodTypeInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_type_info);

        // Initialize views first
        TextView tvYourType = findViewById(R.id.tv_your_type);
        TextView tvCanDonateTo = findViewById(R.id.tv_can_donate_to);
        TextView tvCanReceiveFrom = findViewById(R.id.tv_can_receive_from);

        // Handle potential null bloodType
        String bloodType = getIntent().getStringExtra("BLOOD_TYPE");
        if (bloodType == null) {
            finish(); // Close activity if no blood type provided
            return;
        }

        // Set blood type using formatted string resource
        tvYourType.setText(getString(R.string.your_blood_type, bloodType));

        // Set compatibility information
        setCompatibilityInfo(bloodType, tvCanDonateTo, tvCanReceiveFrom);
    }

    private void setCompatibilityInfo(@NonNull String bloodType,
                                      @NonNull TextView tvCanDonateTo,
                                      @NonNull TextView tvCanReceiveFrom) {
        switch(bloodType) {
            case "A+":
                setCompatibilityTexts(tvCanDonateTo, tvCanReceiveFrom,
                        R.string.compatibility_a_plus_donate,
                        R.string.compatibility_a_plus_receive);
                break;
            case "A-":
                setCompatibilityTexts(tvCanDonateTo, tvCanReceiveFrom,
                        R.string.compatibility_a_minus_donate,
                        R.string.compatibility_a_minus_receive);
                break;
            case "B+":
                setCompatibilityTexts(tvCanDonateTo, tvCanReceiveFrom,
                        R.string.compatibility_b_plus_donate,
                        R.string.compatibility_b_plus_receive);
                break;
            case "B-":
                setCompatibilityTexts(tvCanDonateTo, tvCanReceiveFrom,
                        R.string.compatibility_b_minus_donate,
                        R.string.compatibility_b_minus_receive);
                break;
            case "O+":
                setCompatibilityTexts(tvCanDonateTo, tvCanReceiveFrom,
                        R.string.compatibility_o_plus_donate,
                        R.string.compatibility_o_plus_receive);
                break;
            case "O-":
                setCompatibilityTexts(tvCanDonateTo, tvCanReceiveFrom,
                        R.string.compatibility_o_minus_donate,
                        R.string.compatibility_o_minus_receive);
                break;
            case "AB+":
                setCompatibilityTexts(tvCanDonateTo, tvCanReceiveFrom,
                        R.string.compatibility_ab_plus_donate,
                        R.string.compatibility_ab_plus_receive);
                break;
            case "AB-":
                setCompatibilityTexts(tvCanDonateTo, tvCanReceiveFrom,
                        R.string.compatibility_ab_minus_donate,
                        R.string.compatibility_ab_minus_receive);
                break;
            default:
                // Handle unknown blood type
                tvCanDonateTo.setText(R.string.unknown_blood_type);
                tvCanReceiveFrom.setText(R.string.unknown_blood_type);
        }
    }

    private void setCompatibilityTexts(@NonNull TextView donateToView,
                                       @NonNull TextView receiveFromView,
                                       int donateResId,
                                       int receiveResId) {
        donateToView.setText(getString(R.string.can_donate_to, getString(donateResId)));
        receiveFromView.setText(getString(R.string.can_receive_from, getString(receiveResId)));
    }
}