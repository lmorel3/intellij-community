// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.options

import com.intellij.application.options.editor.CheckboxDescriptor
import com.intellij.application.options.editor.checkBox
import com.intellij.openapi.application.ApplicationBundle
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.UiDslConfigurable
import com.intellij.openapi.vcs.VcsApplicationSettings
import com.intellij.openapi.vcs.impl.LineStatusTrackerSettingListener
import com.intellij.ui.layout.*

private val vcsSettings get() = VcsApplicationSettings.getInstance()

private val cdShowLSTInGutterCheckBox
  get() = CheckboxDescriptor(ApplicationBundle.message("editor.options.highlight.modified.line"), vcsSettings::SHOW_LST_GUTTER_MARKERS)
private val cdShowWhitespacesInLSTGutterCheckBox
  get() = CheckboxDescriptor(ApplicationBundle.message("editor.options.whitespace.line.color"), vcsSettings::SHOW_WHITESPACES_IN_LST)

class VcsGeneralEditorOptionsExtension : UiDslConfigurable.Simple() {
  override fun RowBuilder.createComponentRow() {
    titledRow(ApplicationBundle.message("editor.options.gutter.group")) {
      fun fireLSTSettingsChanged() {
        ApplicationManager.getApplication().messageBus.syncPublisher(LineStatusTrackerSettingListener.TOPIC).settingsUpdated()
      }
      row {
        val showLstGutter = checkBox(cdShowLSTInGutterCheckBox)
          .onApply(::fireLSTSettingsChanged)
        row {
          checkBox(cdShowWhitespacesInLSTGutterCheckBox)
            .enableIf(showLstGutter.selected)
            .onApply(::fireLSTSettingsChanged)
        }
      }
    }
  }
}