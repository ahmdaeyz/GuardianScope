package dev.ahmdaeyz.guardianscope.ui.reader;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;

import dev.ahmdaeyz.guardianscope.R;
import dev.ahmdaeyz.guardianscope.data.model.theguardian.ArticleWithBody;
import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepositoryImpl;
import dev.ahmdaeyz.guardianscope.databinding.FragmentReaderBinding;

import static dev.ahmdaeyz.guardianscope.ui.browser.common.Binding.formatDate;


public class ReaderFragment extends Fragment {

    private static final String ARG_API_URL = "apiUrl";
    private String apiUrl;
    private ReaderViewModel viewModel;
    private String webUrl;

    public ReaderFragment() {
        // Required empty public constructor
    }

    public static ReaderFragment newInstance(String apiUrl) {
        ReaderFragment fragment = new ReaderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_API_URL, apiUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            apiUrl = getArguments().getString(ARG_API_URL);
        }
        ArticlesRepositoryImpl repository = ArticlesRepositoryImpl.getInstance();
        ReaderViewModelFactory factory = new ReaderViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(ReaderViewModel.class);
        viewModel.apiUrlIs(apiUrl);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentReaderBinding binding = FragmentReaderBinding.inflate(inflater, container, false);

        viewModel.article.observe(getViewLifecycleOwner(), (article) -> {
            if (article != null) {
                bindArticle(binding, article);
                webUrl = article.getWebUrl();
            } else {
                disableButtons(binding);
            }
        });
        binding.goBackButton.setOnClickListener((view) -> {
//            Navigation.findNavController(requireActivity(),R.id.nav_host_fragment).navigateUp();
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack();
        });

        binding.shareToTwitterButton.setOnClickListener((view) -> {
            shareTo(view, "Twitter", "com.twitter.android");
        });

        binding.shareButton.setOnClickListener((view) -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, createTextualRep(webUrl));
            Intent.createChooser(shareIntent, null);
            startActivity(shareIntent);
        });

        binding.bookmarkButton.setOnClickListener((view) -> {
            ImageButton bookmarkButton = (ImageButton) view;
            if (bookmarkButton.getTag().equals(R.drawable.ic_bookmark_filled_24)) {
                bookmarkButton.setImageResource(R.drawable.ic_bookmark_24);
                bookmarkButton.setTag(R.drawable.ic_bookmark_24);
                viewModel.unBookmarkArticle(viewModel.article.getValue());
            } else {
                bookmarkButton.setImageResource(R.drawable.ic_bookmark_filled_24);
                bookmarkButton.setTag(R.drawable.ic_bookmark_filled_24);
                viewModel.bookmarkArticle(viewModel.article.getValue());
            }
        });

        return binding.getRoot();
    }

    private void shareTo(View view, String applicationName, String applicationPackage) {
        Intent appLauncher = requireContext().getPackageManager().getLaunchIntentForPackage(applicationPackage);
        if (appLauncher != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, createTextualRep(webUrl));
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

    private void bindArticle(FragmentReaderBinding binding, ArticleWithBody article) {
        binding.articleBody.setHtml(article.getBody(), new HtmlHttpImageGetter(binding.articleBody));
        binding.articleBody.setRemoveTrailingWhiteSpace(true);
        Glide.with(binding.articleImage)
                .load(article.getThumbnail())
                .into(binding.articleImage);
        binding.articleTitle.setText(article.getHeadline() != null ? article.getHeadline() : article.getWebTitle());
        binding.sectionName.setText(article.getSectionName());
        binding.articlePubDate.setText(formatDate(article.getWebPublicationDate()));
        if (article.isBookmarked()) {
            binding.bookmarkButton.setImageResource(R.drawable.ic_bookmark_filled_24);
            binding.bookmarkButton.setTag(R.drawable.ic_bookmark_filled_24);
        } else {
            binding.bookmarkButton.setTag(R.drawable.ic_bookmark_24);
        }
    }

    private void disableButtons(FragmentReaderBinding binding) {
        binding.bookmarkButton.setEnabled(false);
        binding.shareToTwitterButton.setEnabled(false);
        binding.shareButton.setEnabled(false);
    }

}