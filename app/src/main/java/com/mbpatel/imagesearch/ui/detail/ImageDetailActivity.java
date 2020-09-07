package com.mbpatel.imagesearch.ui.detail;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.mbpatel.imagesearch.Injection;
import com.mbpatel.imagesearch.R;
import com.mbpatel.imagesearch.databinding.ActivityImageDetailBinding;
import com.squareup.picasso.Picasso;

import static com.mbpatel.imagesearch.utils.Constants.KEY_IMAGE_ID;
import static com.mbpatel.imagesearch.utils.Constants.KEY_IMAGE_LINK;
import static com.mbpatel.imagesearch.utils.Constants.KEY_IMAGE_TITLE;
import static com.mbpatel.imagesearch.utils.UtilsKt.showSnackBar;
import static com.mbpatel.imagesearch.utils.UtilsKt.showToast;

public class ImageDetailActivity extends AppCompatActivity {

    ImageDetailViewModel mViewModel;
    Boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        ActivityImageDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_image_detail);
        if (!TextUtils.isEmpty(getIntent().getStringExtra(KEY_IMAGE_LINK)))
            Picasso.get().load(getIntent().getStringExtra(KEY_IMAGE_LINK)).placeholder(R.color.gray)
                    .into(binding.ivSearchImage);

        mViewModel = new ViewModelProvider(
                getViewModelStore(),
                Injection.INSTANCE.provideImageDetailViewModelFactory(this, getIntent().getStringExtra(KEY_IMAGE_ID))
        ).get(ImageDetailViewModel.class);

        binding.setViewModel(mViewModel);
        binding.setLifecycleOwner(this);
        binding.setCallback(() -> {
            if (TextUtils.isEmpty(binding.edtComment.getText().toString()))
                showToast(ImageDetailActivity.this, getString(R.string.error_empty_comment));
            else {
                mViewModel.addComment(binding.edtComment.getText().toString(), isEditMode);
                showSnackBar(binding.root, isEditMode ? getString(R.string.success_comment_updated) : getString(R.string.success_comment_added));
            }

        });

        mViewModel.getFetchComment().observe(this, comment -> {
            if (comment != null)
                isEditMode = true;
        });
    }

    private void setActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(getIntent().getStringExtra(KEY_IMAGE_TITLE));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}