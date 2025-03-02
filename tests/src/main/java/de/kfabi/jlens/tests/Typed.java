package de.kfabi.jlens.tests;

import de.kfabi.jlens.Lenses;

@Lenses
public record Typed<T>(T t) {}
