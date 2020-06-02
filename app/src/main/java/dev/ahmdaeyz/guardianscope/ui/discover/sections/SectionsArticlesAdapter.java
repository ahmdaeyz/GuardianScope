package dev.ahmdaeyz.guardianscope.ui.discover.sections;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.ahmdaeyz.guardianscope.data.model.theguardian.Article;
import dev.ahmdaeyz.guardianscope.databinding.ArticleLayoutBinding;
import dev.ahmdaeyz.guardianscope.ui.discover.common.ArticlesAdapter;

import static dev.ahmdaeyz.guardianscope.ui.discover.common.Binding.bindImageViewWithRoundCorners;
import static dev.ahmdaeyz.guardianscope.ui.discover.common.Binding.formatDate;

public class SectionsArticlesAdapter extends ArticlesAdapter<SectionsArticlesAdapter.ViewHolder> {
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
        private final ArticleLayoutBinding binding;

        public ViewHolder(ArticleLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        static ViewHolder from(ViewGroup parent) {
            ArticleLayoutBinding binding = ArticleLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        public void bind(Article item, OnItemClickListener onItemClickListener) {
            binding.articleAuthorName.setText(item.getFields().getAuthor());
            binding.articleTitle.setText(item.getWebTitle());
            binding.articlePubDate.setText(formatDate(item.getWebPublicationDate()));
            bindImageViewWithRoundCorners(binding.articleThumbnail, item.getFields().getThumbnail());
            binding.getRoot().setOnClickListener((view) -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, item);
                }
            });
        }
    }

}