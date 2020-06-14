package dev.ahmdaeyz.guardianscope.ui.browser.discover.sections;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.stream.Collectors;

import dev.ahmdaeyz.guardianscope.data.model.theguardian.Article;
import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepository;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

class SectionsViewModel extends ViewModel {

    private final ArticlesRepository repository;
    public BehaviorSubject<String> currentSection;
    private CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<List<Article>> _articles = new MutableLiveData<>();
    public LiveData<List<Article>> articles = _articles;
    private BehaviorSubject<List<Article>> internalArticles;
    private BehaviorSubject<List<String>> sections = BehaviorSubject.create();

    public SectionsViewModel(ArticlesRepository articlesRepository) {
        this.repository = articlesRepository;
        this.currentSection = BehaviorSubject.createDefault("Politics");
        this.internalArticles = BehaviorSubject.create();
        getArticles();
    }

    private void watchForSectionChanges() {
        disposables.add(
                currentSection
                        .flatMap((currentSection) -> {
                            Log.d("CurrentSection", currentSection);
                            return internalArticles.flatMap(articles -> Observable.just(articles.stream().filter(article -> article.getSectionName().equals(currentSection)).collect(Collectors.toList())));
                        }).subscribe((articles -> _articles.postValue(articles))));
    }


    public void setSections(List<String> sections) {
        this.sections.onNext(sections);
    }

    private void getArticles() {
        disposables.add(
                sections
                        .filter((theSections) -> !theSections.isEmpty())
                        .flatMap(repository::getSectionsArticles)
                        .cache()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((articles) -> {
                            internalArticles.onNext(articles);
                        }, (throwable) -> {
                            Log.d("SectionsViewModel", throwable.toString());
                        })
        );
        watchForSectionChanges();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
