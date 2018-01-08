# frozen_string_literal: true
source "https://rubygems.org"

gem "babelish", :git => "https://github.com/thii/Babelish.git", :branch => "update-google-drive"
gem "danger"
gem "danger-android_lint"
gem "fastlane"

plugins_path = File.join(File.dirname(__FILE__), 'fastlane', 'Pluginfile')
eval_gemfile(plugins_path) if File.exist?(plugins_path)
