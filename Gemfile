# frozen_string_literal: true
source "https://rubygems.org"

gem "babelish"
gem "danger"
gem "danger-android_lint"
gem "fastlane"

plugins_path = File.join(File.dirname(__FILE__), 'fastlane', 'Pluginfile')
eval_gemfile(plugins_path) if File.exist?(plugins_path)
