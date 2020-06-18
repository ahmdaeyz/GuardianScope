package dev.ahmdaeyz.guardianscope.ui.browser.discover.trending;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.ahmdaeyz.guardianscope.data.model.theguardian.Article;
import dev.ahmdaeyz.guardianscope.databinding.TrendingArticleLayoutBinding;
import dev.ahmdaeyz.guardianscope.ui.browser.common.ArticlesAdapter;

import static dev.ahmdaeyz.guardianscope.ui.browser.common.Binding.bindImageViewWithRoundCorners;
import static dev.ahmdaeyz.guardianscope.ui.browser.common.Binding.formatDate;

public class TrendingArticlesAdapter extends ArticlesAdapter<TrendingArticlesAdapter.ViewHolder> {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Article item = getItem(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.bind(item, onItemClickListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TrendingArticleLayoutBinding binding;

        public ViewHolder(TrendingArticleLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        static ViewHolder from(ViewGroup parent) {
            TrendingArticleLayoutBinding binding = TrendingArticleLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        public void bind(Article item, OnItemClickListener onItemClickListener) {
            binding.articleAuthorName.setText("By " + item.getAuthor().replace("(new)", ""));
            binding.articleTitle.setText(item.getHeadline());
            binding.sectionName.setText(item.getSectionName());
            binding.articlePubDate.setText(formatDate(item.getWebPublicationDate()));
            bindImageViewWithRoundCorners(binding.articleThumbnail, item.getThumbnail());
            binding.getRoot().setOnClickListener((view) -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, item);
                }
            });
        }
    }

}
