plugins {
  id("me.roundaround.allay")
}

allay {
  displayName.set("Sprint Indicator")
  description.set("Simple UI element showing whether you're currently sprinting.")
  authors.set(listOf("Roundaround"))
  license.set("MIT")
  homepage.set("https://modrinth.com/mod/sprint-indicator")
  repository.set("https://github.com/Roundaround/mc-sprint-indicator")
  issues.set("https://github.com/Roundaround/mc-sprint-indicator/issues")
  logoFile.set("assets/sprintindicator/banner.png")

  modrinth {
    projectId.set("sprint-indicator")
  }

  release {
    versionType.set("release")
    sourcesJar.set(true)
  }
}
