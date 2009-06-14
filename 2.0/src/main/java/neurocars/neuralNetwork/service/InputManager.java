package neurocars.neuralNetwork.service;

import neurocars.neuralNetwork.DataItem;

public interface InputManager {

	/**
	 * Otevre trenovaci soubor
	 */
	public abstract void initTrainData();

	/**
	 * Nastavi ukazatel na prvni radek, aby se mohlo prochazet znova
	 */
	public abstract void resetTrainData();

	/**
	 * @return dalsi DataItem nebo null, pokud je konec souboru
	 */
	public abstract DataItem getNextTrainItem();

	/**
	 * Otevre testovaci soubor
	 */
	public abstract void initTestData();

	/**
	 * Nastavi ukazatel na novy radek, aby se mohlo prochazet znova
	 */
	public abstract void resetTestData();

	/**
	 * @return dalsi DataItem nebo null, pokud je konec souboru
	 */
	public abstract DataItem getNextTestItem();

	public abstract boolean containsTestData();

}