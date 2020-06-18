package dev.ahmdaeyz.guardianscope.ui.browser.bookmarks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import dev.ahmdaeyz.guardianscope.data.model.theguardian.ArticleWithBody;
import dev.ahmdaeyz.guardianscope.databinding.BookmarkedArticleLayoutBinding;
import dev.ahmdaeyz.guardianscope.ui.browser.common.ArticlesAdapter;

import static dev.ahmdaeyz.guardianscope.ui.browser.common.Binding.formatDate;

class BookmarksAdapter extends ArticlesAdapter<BookmarksAdapter.ViewHolder> {
    private OnDestroyItemListener onDestroyItemListener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ArticleWithBody article = (ArticleWithBody) getItem(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.bind(article, onDestroyItemListener, onItemClickListener);
    }

    void setOnDestroyItemListener(OnDestroyItemListener onDestroyItemListener) {
        this.onDestroyItemListener = onDestroyItemListener;
    }

    public interface OnDestroyItemListener {
        void onDestroy(View view, ArticleWithBody article);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        BookmarkedArticleLayoutBinding binding;

        public ViewHolder(BookmarkedArticleLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        static ViewHolder from(ViewGroup parent) {
            BookmarkedArticleLayoutBinding binding = BookmarkedArticleLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        public void bind(ArticleWithBody article, OnDestroyItemListener onDestroyItemListener, OnItemClickListener onItemClickListener) {
            Glide.with(binding.articleImage)
                    .load(article.getThumbnail())
                    .into(binding.articleImage);
            binding.articleTitle.setText(article.getHeadline() != null ? article.getHeadline() : article.getWebTitle());
            binding.sectionName.setText(article.getSectionName());
            binding.articlePubDate.setText(formatDate(article.getWebPublicationDate()));
            binding.getRoot().setOnClickListener(itemView -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(itemView, article);
                }
            });
            binding.destroyButton.setOnClickListener(itemView -> {
                if (onDestroyItemListener != null) {
                    onDestroyItemListener.onDestroy(itemView, article);
                }
            });
        }
    }
}
