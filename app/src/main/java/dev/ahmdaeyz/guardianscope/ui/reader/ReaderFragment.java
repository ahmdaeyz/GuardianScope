package dev.ahmdaeyz.guardianscope.ui.reader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import org.parceler.Parcels;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;

import dev.ahmdaeyz.guardianscope.R;
import dev.ahmdaeyz.guardianscope.data.model.theguardian.Article;
import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepository;
import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepositoryImpl;
import dev.ahmdaeyz.guardianscope.databinding.FragmentReaderBinding;
import dev.ahmdaeyz.guardianscope.navigation.NavigateFrom;

import static dev.ahmdaeyz.guardianscope.ui.browser.discover.common.Binding.formatDate;


public class ReaderFragment extends Fragment {

    private static final String ARG_ARTICLE = "article";
    private Article article;
    private FragmentReaderBinding binding;
    private NavigateFrom.Reader navigateFromReader;
    private ReaderViewModel viewModel;

    public ReaderFragment() {
        // Required empty public constructor
    }

    public static ReaderFragment newInstance(Parcelable article) {
        ReaderFragment fragment = new ReaderFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ARTICLE, article);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            navigateFromReader = (NavigateFrom.Reader) context;
        } catch (ClassCastException e) {
            Log.e("ReaderFragment", "Parent Activity must implement NavigateFrom.Reader");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            article = Parcels.unwrap(getArguments().getParcelable(ARG_ARTICLE));
        }
        ArticlesRepository repository = ArticlesRepositoryImpl.getInstance();
        ReaderViewModelFactory factory = new ReaderViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(ReaderViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReaderBinding.inflate(inflater, container, false);
        bindArticle();
        binding.goBackButton.setOnClickListener((view) -> {
            navigateFromReader.onBackPressedFromFragment();
        });
        binding.shareToTwitterButton.setOnClickListener((view) -> {
            shareTo(view, "Twitter", "com.twitter.android");
        });
        binding.shareButton.setOnClickListener((view) -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, createTextualRep(article.getWebUrl()));
            Intent.createChooser(shareIntent, null);
            startActivity(shareIntent);
        });
        binding.favouriteButton.setOnClickListener((view) -> {
            viewModel.favouriteArticle(article);
            ImageButton likeButton = (ImageButton) view;
            if (article.isLiked()) {
                likeButton.setImageResource(R.drawable.ic_heart_filled_24);
                article.setLiked(false);
            } else {
                likeButton.setImageResource(R.drawable.ic_heart_24);
                article.setLiked(true);
            }
        });
        binding.bookmarkButton.setOnClickListener((view) -> {
            viewModel.bookmarkArticle(article);
            ImageButton bookmarkButton = (ImageButton) view;
            if (article.isBookmarked()) {
                bookmarkButton.setImageResource(R.drawable.ic_bookmark_filled_24);
                article.setBookmarked(false);
            } else {
                bookmarkButton.setImageResource(R.drawable.ic_bookmark_24);
                article.setBookmarked(true);
            }
        });
        return binding.getRoot();
    }

    private void shareTo(View view, String applicationName, String applicationPackage) {
        Intent appLauncher = requireContext().getPackageManager().getLaunchIntentForPackage(applicationPackage);
        if (appLauncher != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, createTextualRep(article.getWebUrl()));
            shareIntent.setType("text/plain");
            shareIntent.setPackage(applicationPackage);
        } else {
            Snackbar.make(
                    view,
                    applicationName + " app isn't installed.",
                    Snackbar.LENGTH_SHORT
            ).show();
        }
    }

    private String createTextualRep(String webTitle) {
        return "Read this interesting article at " + webTitle;
    }

    private void bindArticle() {
        binding.articleBody.setHtml(article.getFields().getBody(), new HtmlHttpImageGetter(binding.articleBody));
        Glide.with(binding.articleImage)
                .load(article.getFields().getThumbnail())
                .into(binding.articleImage);
        binding.articleTitle.setText(article.getFields().getHeadline() != null ? article.getFields().getHeadline() : article.getWebTitle());
        binding.sectionName.setText(article.getSectionName());
        binding.articlePubDate.setText(formatDate(article.getWebPublicationDate()));
    }
}