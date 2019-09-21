package com.sample.zomatodemo.injection.component;

import com.sample.zomatodemo.injection.module.NetworkModule;
import com.sample.zomatodemo.injection.module.RxBusModule;
import com.sample.zomatodemo.ui.base.BaseActivity;
import com.sample.zomatodemo.ui.base.BaseFragment;
import com.sample.zomatodemo.ui.base.BaseViewHolder;
import com.sample.zomatodemo.ui.base.BaseViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class, RxBusModule.class})
public interface ApplicationComponent {

    void inject(BaseActivity mainActivity);

    void inject(BaseFragment baseFragment);

    void inject(BaseViewModel baseViewModel);

}
