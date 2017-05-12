PhyloGeoTool is a web-application to interactively navigate large phylogenies and to explore associated clinical and epidemiological data. PhyloGeoTool implements an algorithm that automatically partitions a phylogeny into an optimal number of clusters, thereby recursively partitioning each identified cluster. A graphical user interface provides a concise visualisation of the different levels of the phylogenetic tree and the attributes (geographical, clinical, demographical) associated with the sequences. Phylogenetic placement of new sequences onto an existing tree is made possible through pplacer. Below we show a screenshot that shows PhyloGeoTool's user interface.

PhyloGeoTool has been built to be user-friendly in use, only requiring a phylogenetic tree and a text file describing the attributes of the sequences. A demonstration of PhyloGeoTool is presented in a short [video](https://youtu.be/xiYODenyEIQ).  A [user instruction manual](https://github.com/rega-cev/phylogeotool/blob/master/doc/UserManual.pdf) is available to provide on information the use of the application. For users who whant to host their own instance of the tool, an [installation manual](https://github.com/rega-cev/phylogeotool/blob/master/doc/InstallationManual.pdf) is available.  

The methodology paper describing PhyloGeoTool has been submitted for peer-review. A reference to the publication as well as a citation to cite PhyloGeoTool will be provided as soon as the paper has become publicly available.

Deployed instances of the PhyloGeoTool can be found [here](http://phylogeotool.gbiomed.kuleuven.be/euresist/) and [here](http://phylogeotool.gbiomed.kuleuven.be/dengue/).

The first release of the source code is [available here](https://github.com/rega-cev/phylogeotool/releases/tag/1.0.0).

![PhyloGeoTool user interface](https://github.com/rega-cev/phylogeotool/blob/master/phylogeotool.png)
*Caption: The PhyloGeoTool graphical user interface.
The upper left panel shows the geographical distribution of the samples present in the selected cluster. The lower left panel shows the distribution for a selected trait of interest; white bars show the distribution for the entire data set for that level of the tree, whereas the colored bars show the distribution for a specific selected cluster. The right panel shows the clustered phylogenetic tree and allows to perform phylogenetic placement.*
