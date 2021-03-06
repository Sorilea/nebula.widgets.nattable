/*******************************************************************************
 * Copyright (c) 2013 Dirk Fauth and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Dirk Fauth <dirk.fauth@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.nebula.widgets.nattable.examples._800_Integration;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.command.VisualRefreshCommand;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.ExtendedReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultDoubleDisplayConverter;
import org.eclipse.nebula.widgets.nattable.examples.AbstractNatExample;
import org.eclipse.nebula.widgets.nattable.examples.data.person.ExtendedPersonWithAddress;
import org.eclipse.nebula.widgets.nattable.examples.data.person.PersonService;
import org.eclipse.nebula.widgets.nattable.examples.runner.StandaloneNatExampleRunner;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsEventLayer;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsSortModel;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.groupBy.DarkGroupByThemeExtension;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.groupBy.DefaultGroupByThemeExtension;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.groupBy.GroupByConfigAttributes;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.groupBy.GroupByDataLayer;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.groupBy.GroupByHeaderLayer;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.groupBy.GroupByHeaderMenuConfiguration;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.groupBy.GroupByModel;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.groupBy.ModernGroupByThemeExtension;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.groupBy.summary.IGroupBySummaryProvider;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.groupBy.summary.SummationGroupBySummaryProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultSummaryRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultColumnHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultRowHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.hideshow.ColumnHideShowLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayerTransform;
import org.eclipse.nebula.widgets.nattable.layer.CompositeLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.LabelStack;
import org.eclipse.nebula.widgets.nattable.layer.cell.AbstractOverrider;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.painter.NatTableBorderOverlayPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.CheckBoxPainter;
import org.eclipse.nebula.widgets.nattable.persistence.command.DisplayPersistenceDialogCommandHandler;
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.sort.SortHeaderLayer;
import org.eclipse.nebula.widgets.nattable.sort.config.SingleClickSortConfiguration;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.HorizontalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.style.IStyle;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.style.theme.DarkNatTableThemeConfiguration;
import org.eclipse.nebula.widgets.nattable.style.theme.DefaultNatTableThemeConfiguration;
import org.eclipse.nebula.widgets.nattable.style.theme.ModernNatTableThemeConfiguration;
import org.eclipse.nebula.widgets.nattable.style.theme.ThemeConfiguration;
import org.eclipse.nebula.widgets.nattable.summaryrow.ISummaryProvider;
import org.eclipse.nebula.widgets.nattable.summaryrow.SummaryDisplayConverter;
import org.eclipse.nebula.widgets.nattable.summaryrow.SummaryRowConfigAttributes;
import org.eclipse.nebula.widgets.nattable.summaryrow.SummaryRowLayer;
import org.eclipse.nebula.widgets.nattable.summaryrow.SummationSummaryProvider;
import org.eclipse.nebula.widgets.nattable.tree.TreeLayer;
import org.eclipse.nebula.widgets.nattable.tree.command.TreeCollapseAllCommand;
import org.eclipse.nebula.widgets.nattable.tree.command.TreeExpandAllCommand;
import org.eclipse.nebula.widgets.nattable.ui.menu.AbstractHeaderMenuConfiguration;
import org.eclipse.nebula.widgets.nattable.ui.menu.DebugMenuConfiguration;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuBuilder;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.TransformedList;

/**
 * Simple example showing how to add the group by feature to the layer
 * composition of a grid in conjunction with showing summary values of groupings.
 * 
 * @author Dirk Fauth
 *
 */
public class _809_GroupBySummarySummaryRowExample extends AbstractNatExample {

	private static final String ROW_HEADER_SUMMARY_ROW = "rowHeaderSummaryRowLabel";
	
	private IGroupBySummaryProvider<ExtendedPersonWithAddress> sumMoneyGroupBySummaryProvider;
	private IGroupBySummaryProvider<ExtendedPersonWithAddress> avgMoneyGroupBySummaryProvider;
	
	private ISummaryProvider sumMoneySummaryProvider;
	private ISummaryProvider avgMoneySummaryProvider;
	
	private boolean useMoneySum = true;
	
	private int currentTheme = 1;

	public static void main(String[] args) throws Exception {
		StandaloneNatExampleRunner.run(800, 600, new _809_GroupBySummarySummaryRowExample());
	}

	@Override
	public String getDescription() {
		return "This example shows the usage of the group by feature in conjunction with summary values of the groupings.";
	}
	
	@Override
	public Control createExampleControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout());
		
		//create a new ConfigRegistry which will be needed for GlazedLists handling
		final ConfigRegistry configRegistry = new ConfigRegistry();

		//property names of the ExtendedPersonWithAddress class
		String[] propertyNames = {"firstName", "lastName", "age", "money",
				"married", "gender", "birthday"};

		//mapping from property to label, needed for column header labels
		Map<String, String> propertyToLabelMap = new HashMap<String, String>();
		propertyToLabelMap.put("firstName", "Firstname");
		propertyToLabelMap.put("lastName", "Lastname");
		propertyToLabelMap.put("age", "Age");
		propertyToLabelMap.put("money", "Money");
		propertyToLabelMap.put("married", "Married");
		propertyToLabelMap.put("gender", "Gender");
		propertyToLabelMap.put("birthday", "Birthday");

		final IColumnPropertyAccessor<ExtendedPersonWithAddress> columnPropertyAccessor = 
				new ExtendedReflectiveColumnPropertyAccessor<ExtendedPersonWithAddress>(propertyNames);
		
		//to enable the group by summary feature, the GroupByDataLayer needs to know the ConfigRegistry
		List<ExtendedPersonWithAddress> persons = PersonService.getExtendedPersonsWithAddress(10); 
		final BodyLayerStack<ExtendedPersonWithAddress> bodyLayerStack = 
				new BodyLayerStack<ExtendedPersonWithAddress>(
						persons, columnPropertyAccessor, configRegistry);

		bodyLayerStack.getBodyDataLayer().setConfigLabelAccumulator(new ColumnLabelAccumulator());
		
		//build the column header layer
		IDataProvider columnHeaderDataProvider = new DefaultColumnHeaderDataProvider(propertyNames, propertyToLabelMap);
		DataLayer columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);
		ILayer columnHeaderLayer = new ColumnHeaderLayer(columnHeaderDataLayer, bodyLayerStack, bodyLayerStack.getSelectionLayer());
		
		//add sorting
		SortHeaderLayer<ExtendedPersonWithAddress> sortHeaderLayer = new SortHeaderLayer<ExtendedPersonWithAddress>(
				columnHeaderLayer, 
				new GlazedListsSortModel<ExtendedPersonWithAddress>(
						bodyLayerStack.getSortedList(), 
						columnPropertyAccessor,
						configRegistry, 
						bodyLayerStack.getBodyDataLayer()), 
				false);
		
		//connect sortModel to GroupByDataLayer to support sorting by group by summary values
		bodyLayerStack.getBodyDataLayer().setSortModel(sortHeaderLayer.getSortModel());

		//build the row header layer
		// Adding the specialized DefaultSummaryRowHeaderDataProvider to indicate the summary row in the row header
		IDataProvider rowHeaderDataProvider = new DefaultSummaryRowHeaderDataProvider(
				bodyLayerStack.getBodyDataProvider(), "\u2211");
		final DataLayer rowHeaderDataLayer = new DefaultRowHeaderDataLayer(rowHeaderDataProvider);
		//add a label to the row header summary row cell aswell, so it can be styled differently too
		//in this case it will simply use the same styling as the summary row in the body
		rowHeaderDataLayer.setConfigLabelAccumulator(new AbstractOverrider() {
			@Override
			public void accumulateConfigLabels(LabelStack configLabels, int columnPosition, int rowPosition) {
				if ((rowPosition+1) == rowHeaderDataLayer.getRowCount()) {
					configLabels.addLabel(ROW_HEADER_SUMMARY_ROW);
					configLabels.addLabel(SummaryRowLayer.DEFAULT_SUMMARY_ROW_CONFIG_LABEL);
				}
			}
		});
		ILayer rowHeaderLayer = new RowHeaderLayer(rowHeaderDataLayer, bodyLayerStack, bodyLayerStack.getSelectionLayer());
		
		//build the corner layer
		IDataProvider cornerDataProvider = new DefaultCornerDataProvider(columnHeaderDataProvider, rowHeaderDataProvider);
		DataLayer cornerDataLayer = new DataLayer(cornerDataProvider);
		ILayer cornerLayer = new CornerLayer(cornerDataLayer, rowHeaderLayer, sortHeaderLayer);
		
		//build the grid layer
		GridLayer gridLayer = new GridLayer(bodyLayerStack, sortHeaderLayer, rowHeaderLayer, cornerLayer);
		
		//set the group by header on top of the grid
		CompositeLayer compositeGridLayer = new CompositeLayer(1, 2);
		final GroupByHeaderLayer groupByHeaderLayer = 
				new GroupByHeaderLayer(bodyLayerStack.getGroupByModel(), gridLayer, columnHeaderDataProvider);
		compositeGridLayer.setChildLayer(GroupByHeaderLayer.GROUP_BY_REGION, groupByHeaderLayer, 0, 0);
		compositeGridLayer.setChildLayer("Grid", gridLayer, 0, 1);
		
		//turn the auto configuration off as we want to add our header menu configuration
		final NatTable natTable = new NatTable(container, compositeGridLayer, false);
		
		//as the autoconfiguration of the NatTable is turned off, we have to add the 
		//DefaultNatTableStyleConfiguration and the ConfigRegistry manually	
		natTable.setConfigRegistry(configRegistry);
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());

		//add some additional styling
		natTable.addConfiguration(new AbstractRegistryConfiguration() {
			
			@Override
			public void configureRegistry(IConfigRegistry configRegistry) {
				configRegistry.registerConfigAttribute(
						CellConfigAttributes.CELL_PAINTER, 
						new CheckBoxPainter(), 
						DisplayMode.NORMAL, 
						ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + 4);
				
				IStyle style = new Style();
				style.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, HorizontalAlignmentEnum.RIGHT);
				configRegistry.registerConfigAttribute(
						CellConfigAttributes.CELL_STYLE, 
						style, 
						DisplayMode.NORMAL, 
						ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + 2);
				configRegistry.registerConfigAttribute(
						CellConfigAttributes.CELL_STYLE, 
						style, 
						DisplayMode.NORMAL, 
						ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + 3);

				configRegistry.registerConfigAttribute(
						CellConfigAttributes.DISPLAY_CONVERTER, 
						new DefaultDoubleDisplayConverter(), 
						DisplayMode.NORMAL, 
						ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + 3);

				//the main styling of the summary row cell in the row header is done via
				//summary row default style, but we need to override the alignment
				style = new Style();
				style.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, HorizontalAlignmentEnum.CENTER);
				configRegistry.registerConfigAttribute(
						CellConfigAttributes.CELL_STYLE, 
						style, 
						DisplayMode.NORMAL, 
						ROW_HEADER_SUMMARY_ROW);
				configRegistry.registerConfigAttribute(
						CellConfigAttributes.CELL_STYLE, 
						style, 
						DisplayMode.SELECT, 
						ROW_HEADER_SUMMARY_ROW);
			}
		});
		
		//add sorting configuration
		natTable.addConfiguration(new SingleClickSortConfiguration());
		
		sumMoneyGroupBySummaryProvider = 
				new SummationGroupBySummaryProvider<ExtendedPersonWithAddress>(columnPropertyAccessor);
		avgMoneyGroupBySummaryProvider = 
				new AverageMoneyGroupBySummaryProvider();
		
		//create a new IDataProvider that operates on the basic underlying list
		//this is necessary because the IDataProvider in the body layer stack is operating
		//on the TreeList, and on collapsing a node, the children will be not visible, which
		//has effect on the summary value.
		final IDataProvider summaryDataProvider = 
				new ListDataProvider<ExtendedPersonWithAddress>(persons, columnPropertyAccessor);
		sumMoneySummaryProvider = 
				new SummationSummaryProvider(summaryDataProvider, false);
		avgMoneySummaryProvider = 
				new AverageMoneySummaryProvider(summaryDataProvider);

		//add group by summary configuration
		natTable.addConfiguration(new AbstractRegistryConfiguration() {
			
			@Override
			public void configureRegistry(IConfigRegistry configRegistry) {
				// GroupBy summary configuration
				configRegistry.registerConfigAttribute(GroupByConfigAttributes.GROUP_BY_SUMMARY_PROVIDER,
						sumMoneyGroupBySummaryProvider,
						DisplayMode.NORMAL, GroupByDataLayer.GROUP_BY_COLUMN_PREFIX + 3);

				configRegistry.registerConfigAttribute(GroupByConfigAttributes.GROUP_BY_SUMMARY_PROVIDER,
						new AverageAgeGroupBySummaryProvider(),
						DisplayMode.NORMAL, GroupByDataLayer.GROUP_BY_COLUMN_PREFIX + 2);

				configRegistry.registerConfigAttribute(
						GroupByConfigAttributes.GROUP_BY_CHILD_COUNT_PATTERN,
						"[{0}] - ({1})");
				
				//set a custom display converter to the money groupby column that transforms the values correctly localized
				configRegistry.registerConfigAttribute(
						CellConfigAttributes.DISPLAY_CONVERTER, 
						new SummaryDisplayConverter(new DefaultDoubleDisplayConverter()),
						DisplayMode.NORMAL, 
						GroupByDataLayer.GROUP_BY_SUMMARY_COLUMN_PREFIX + 3);

				// SummaryRow configuration
				configRegistry.registerConfigAttribute(
						SummaryRowConfigAttributes.SUMMARY_PROVIDER,
						sumMoneySummaryProvider,
						DisplayMode.NORMAL,
						SummaryRowLayer.DEFAULT_SUMMARY_COLUMN_CONFIG_LABEL_PREFIX + 3);

				configRegistry.registerConfigAttribute(
						SummaryRowConfigAttributes.SUMMARY_PROVIDER,
						new AverageAgeSummaryProvider(summaryDataProvider),
						DisplayMode.NORMAL,
						SummaryRowLayer.DEFAULT_SUMMARY_COLUMN_CONFIG_LABEL_PREFIX + 2);

				configRegistry.registerConfigAttribute(
						CellConfigAttributes.DISPLAY_CONVERTER, 
						new SummaryDisplayConverter(new DefaultDoubleDisplayConverter()),
						DisplayMode.NORMAL, 
						SummaryRowLayer.DEFAULT_SUMMARY_COLUMN_CONFIG_LABEL_PREFIX + 3);
			}
		});
		
		//add group by header configuration
		natTable.addConfiguration(new GroupByHeaderMenuConfiguration(natTable, groupByHeaderLayer));

		natTable.addConfiguration(new AbstractHeaderMenuConfiguration(natTable) {
			
			@Override
			protected PopupMenuBuilder createColumnHeaderMenu(NatTable natTable) {
				return super.createColumnHeaderMenu(natTable)
							.withHideColumnMenuItem()
							.withShowAllColumnsMenuItem()
							.withStateManagerMenuItemProvider();
			}
			
			@Override
			protected PopupMenuBuilder createCornerMenu(NatTable natTable) {
				return super.createCornerMenu(natTable)
							.withShowAllColumnsMenuItem()
							.withStateManagerMenuItemProvider();
			}
		});
		
		natTable.addConfiguration(new DebugMenuConfiguration(natTable));
		
		natTable.configure();
		
		//set the modern theme to visualize the summary better
		final ThemeConfiguration defaultTheme = new DefaultNatTableThemeConfiguration();
		defaultTheme.addThemeExtension(new DefaultGroupByThemeExtension());
		
		final ThemeConfiguration modernTheme = new ModernNatTableThemeConfiguration();
		modernTheme.addThemeExtension(new ModernGroupByThemeExtension());
		
		final ThemeConfiguration darkTheme = new DarkNatTableThemeConfiguration();
		darkTheme.addThemeExtension(new DarkGroupByThemeExtension());
		
		natTable.setTheme(modernTheme);
		
		//add a border on every side of the table
		natTable.addOverlayPainter(new NatTableBorderOverlayPainter());
		
		natTable.registerCommandHandler(new DisplayPersistenceDialogCommandHandler(natTable));
		
		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
		
		Composite buttonPanel = new Composite(container, SWT.NONE);
		buttonPanel.setLayout(new RowLayout());
		GridDataFactory.fillDefaults().grab(true, false).applyTo(buttonPanel);
		
		Button toggleHeaderButton = new Button(buttonPanel, SWT.PUSH);
		toggleHeaderButton.setText("Toggle Group By Header");
		toggleHeaderButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				groupByHeaderLayer.setVisible(!groupByHeaderLayer.isVisible());
			}
		});
		
		Button collapseAllButton = new Button(buttonPanel, SWT.PUSH);
		collapseAllButton.setText("Collapse All");
		collapseAllButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				natTable.doCommand(new TreeCollapseAllCommand());
			}
		});
		
		Button expandAllButton = new Button(buttonPanel, SWT.PUSH);
		expandAllButton.setText("Expand All");
		expandAllButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				natTable.doCommand(new TreeExpandAllCommand());
			}
		});
		
		Button toggleMoneySummaryButton = new Button(buttonPanel, SWT.PUSH);
		toggleMoneySummaryButton.setText("Toggle Money Group Summary (SUM/AVG)");
		toggleMoneySummaryButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//clear the group by summary cache so the new summary calculation gets triggered
				bodyLayerStack.getBodyDataLayer().clearCache();
				
				useMoneySum = !useMoneySum;
				if (useMoneySum) {
					configRegistry.registerConfigAttribute(
							GroupByConfigAttributes.GROUP_BY_SUMMARY_PROVIDER,
							sumMoneyGroupBySummaryProvider,
							DisplayMode.NORMAL, 
							GroupByDataLayer.GROUP_BY_COLUMN_PREFIX + 3);
					
					configRegistry.registerConfigAttribute(
							SummaryRowConfigAttributes.SUMMARY_PROVIDER,
							sumMoneySummaryProvider,
							DisplayMode.NORMAL,
							SummaryRowLayer.DEFAULT_SUMMARY_COLUMN_CONFIG_LABEL_PREFIX + 3);

				}
				else {
					configRegistry.registerConfigAttribute(
							GroupByConfigAttributes.GROUP_BY_SUMMARY_PROVIDER,
							avgMoneyGroupBySummaryProvider,
							DisplayMode.NORMAL, 
							GroupByDataLayer.GROUP_BY_COLUMN_PREFIX + 3);
					
					configRegistry.registerConfigAttribute(
							SummaryRowConfigAttributes.SUMMARY_PROVIDER,
							avgMoneySummaryProvider,
							DisplayMode.NORMAL,
							SummaryRowLayer.DEFAULT_SUMMARY_COLUMN_CONFIG_LABEL_PREFIX + 3);
				}
				natTable.doCommand(new VisualRefreshCommand());
			}
		});

		Button toggleThemeButton = new Button(buttonPanel, SWT.PUSH);
		toggleThemeButton.setText("Toggle Theme");
		toggleThemeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (currentTheme == 0) {
					natTable.setTheme(modernTheme);
					currentTheme++;
				}
				else if (currentTheme == 1) {
					natTable.setTheme(darkTheme);
					currentTheme++;
				}
				else if (currentTheme == 2) {
					natTable.setTheme(defaultTheme);
					currentTheme = 0;
				}
			}
		});
		
		return container;
	}
	
	/**
	 * Always encapsulate the body layer stack in an AbstractLayerTransform to ensure that the
	 * index transformations are performed in later commands.
	 * @param <T>
	 */
	class BodyLayerStack<T> extends AbstractLayerTransform {
		
		private final SortedList<T> sortedList;
		
		private final IDataProvider bodyDataProvider;
		
		private final GroupByDataLayer<T> bodyDataLayer;
		
		private final SelectionLayer selectionLayer;
		
		private final GroupByModel groupByModel = new GroupByModel();
		
		public BodyLayerStack(List<T> values, IColumnPropertyAccessor<T> columnPropertyAccessor, ConfigRegistry configRegistry) {
			//wrapping of the list to show into GlazedLists
			//see http://publicobject.com/glazedlists/ for further information
			EventList<T> eventList = GlazedLists.eventList(values);
			TransformedList<T, T> rowObjectsGlazedList = GlazedLists.threadSafeList(eventList);
			
			//use the SortedList constructor with 'null' for the Comparator because the Comparator
			//will be set by configuration
			this.sortedList = new SortedList<T>(rowObjectsGlazedList, null);
			
			//Use the GroupByDataLayer instead of the default DataLayer
			this.bodyDataLayer = new GroupByDataLayer<T>(
					getGroupByModel(), sortedList, columnPropertyAccessor, configRegistry);
			//get the IDataProvider that was created by the GroupByDataLayer
			this.bodyDataProvider = bodyDataLayer.getDataProvider();
			
			SummaryRowLayer summaryRowLayer = new SummaryRowLayer(bodyDataLayer, configRegistry, false);
			
			//layer for event handling of GlazedLists and PropertyChanges
			GlazedListsEventLayer<T> glazedListsEventLayer = 
				new GlazedListsEventLayer<T>(summaryRowLayer, sortedList);

			ColumnReorderLayer columnReorderLayer = new ColumnReorderLayer(glazedListsEventLayer);
			ColumnHideShowLayer columnHideShowLayer = new ColumnHideShowLayer(columnReorderLayer);
			this.selectionLayer = new SelectionLayer(columnHideShowLayer);
			
			//add a tree layer to visualise the grouping
			TreeLayer treeLayer = new TreeLayer(selectionLayer, bodyDataLayer.getTreeRowModel());

			ViewportLayer viewportLayer = new ViewportLayer(treeLayer);
			
			setUnderlyingLayer(viewportLayer);
		}

		public SelectionLayer getSelectionLayer() {
			return this.selectionLayer;
		}
		
		public SortedList<T> getSortedList() {
			return this.sortedList;
		}

		public IDataProvider getBodyDataProvider() {
			return this.bodyDataProvider;
		}

		public GroupByDataLayer<T> getBodyDataLayer() {
			return this.bodyDataLayer;
		}
		
		public GroupByModel getGroupByModel() {
			return this.groupByModel;
		}
	}
	
	/**
	 * Example implementation for a typed IGroupBySummaryProvider that calculates the average age of
	 * ExtendedPersonWithAddress objects in a grouping.
	 */
	class AverageAgeGroupBySummaryProvider implements IGroupBySummaryProvider<ExtendedPersonWithAddress> {

		@Override
		public Object summarize(int columnIndex, List<ExtendedPersonWithAddress> children) {
			int summaryValue = 0;
			for (ExtendedPersonWithAddress child : children) {
				summaryValue += child.getAge();
			}
			return summaryValue / (children.size() > 0 ? children.size() : 1);
		}
		
	}
	
	/**
	 * Example implementation for a typed IGroupBySummaryProvider that calculates the average money of
	 * ExtendedPersonWithAddress objects in a grouping.
	 */
	class AverageMoneyGroupBySummaryProvider implements IGroupBySummaryProvider<ExtendedPersonWithAddress> {

		@Override
		public Object summarize(int columnIndex, List<ExtendedPersonWithAddress> children) {
			double summaryValue = 0;
			for (ExtendedPersonWithAddress child : children) {
				summaryValue += child.getMoney();
			}
			
			NumberFormat format = NumberFormat.getInstance();
			format.setMaximumFractionDigits(2);
			
			return format.format(summaryValue / (children.size() > 0 ? children.size() : 1));
		}
		
	}
	
	
	/**
	 * Example implementation for a ISummaryProvider that calculates the average age of
	 * ExtendedPersonWithAddress objects.
	 */
	class AverageAgeSummaryProvider implements ISummaryProvider {

		private IDataProvider dataProvider;
		
		public AverageAgeSummaryProvider(IDataProvider dataProvider) {
			this.dataProvider = dataProvider;
		}
		
		@Override
		public Object summarize(int columnIndex) {
			double total = 0;
			int rowCount = dataProvider.getRowCount();
			int valueRows = 0;

			for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
				Object dataValue = dataProvider.getDataValue(columnIndex, rowIndex);
				//this check is necessary because of the GroupByObject
				if (dataValue instanceof Number) {
					total = total + Double.parseDouble(dataValue.toString());
					valueRows++;
				}
			}
			return "Avg: " + String.format( "%.2f", total / valueRows);
		}
		
	}
	
	/**
	 * Example implementation for a ISummaryProvider that calculates the average money of
	 * ExtendedPersonWithAddress objects.
	 */
	class AverageMoneySummaryProvider implements ISummaryProvider {

		private IDataProvider dataProvider;
		
		public AverageMoneySummaryProvider(IDataProvider dataProvider) {
			this.dataProvider = dataProvider;
		}

		@Override
		public Object summarize(int columnIndex) {
			double total = 0;
			int rowCount = dataProvider.getRowCount();
			int valueRows = 0;

			for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
				Object dataValue = dataProvider.getDataValue(columnIndex, rowIndex);
				//this check is necessary because of the GroupByObject
				if (dataValue instanceof Number) {
					total = total + Double.parseDouble(dataValue.toString());
					valueRows++;
				}
			}
			return "Avg: " + String.format( "%.2f", total / valueRows);
		}
		
	}

}
