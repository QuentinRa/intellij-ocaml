package com.ocaml.ide.settings;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.*;
import org.jetbrains.annotations.*;

@State(name = "ReasonSettings", storages = {@Storage("reason.xml")})
public class ORSettings implements PersistentStateComponent<ORSettings.ReasonSettingsState> {
    private final @NotNull Project m_project;

    // Opam
    private String myOpamLocation = "";
    private String mySwitchName = "";
    private boolean myIsWsl = false;

    private ORSettings(@NotNull Project project) {
        m_project = project;
    }

    @Override
    public @NotNull ReasonSettingsState getState() {
        // maybe we should store a ReasonSettingsState ?
        ReasonSettingsState state = new ReasonSettingsState();
        state.opamLocation = myOpamLocation;
        state.isWsl = myIsWsl;
        state.switchName = mySwitchName;
        return state;
    }

    @Override
    public void loadState(@NotNull ReasonSettingsState state) {
        // maybe we should store a ReasonSettingsState ?
        myOpamLocation = state.opamLocation;
        myIsWsl = state.isWsl;
        mySwitchName = state.switchName;
    }

    public @NotNull Project getProject() {
        return m_project;
    }

    public @NotNull String getOpamLocation() {
        return myOpamLocation == null ? "" : myOpamLocation;
    }

    public void setOpamLocation(@Nullable String location) {
        myOpamLocation = location;
    }

    public boolean isWsl() {
        return myIsWsl;
    }

    public void setIsWsl(boolean isWsl) {
        myIsWsl = isWsl;
    }

    public String getSwitchName() {
        return mySwitchName;
    }

    public void setSwitchName(@Nullable String name) {
        mySwitchName = name == null ? "" : name;
    }

    @SuppressWarnings("WeakerAccess")
    public static class ReasonSettingsState {
        // Opam
        public String opamLocation = "";
        public String switchName = "";
        public boolean isWsl = false;
    }
}
