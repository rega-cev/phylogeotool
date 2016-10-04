#!/bin/bash

if [ "$#" -eq 2 ]; then
    monitor_file="$1/monitor_file"
    cp /dev/null $monitor_file
    echo "Monitor file created"
    tar -zxvf "$2" -C "$1" 2>> $monitor_file
    echo "Files have been untarred to: $1"
    tmp_dir="$1"
    tree_file="$1/phylo.tree"
    alignment_file="$1/align.fasta"
    sequence_file="$1/sequences.fasta"
    log_file="$1/logfile.log"
elif [ "$#" -eq 5 ]; then
    monitor_file="$1/monitor_file"
    cp /dev/null $monitor_file
    echo "Monitor file created"
    tmp_dir="$1"
    tree_file="$2"
    alignment_file="$3"
    sequence_file="$4"
    log_file="$5"
else
    echo "Usage: ./place.sh temp-dir tar-file"
    echo "or"
    echo "Usage: ./place.sh temp-dir tree-file.tree alignment-file.fasta new-sequences-file.fasta log-file.log"
    exit 1
fi

#echo "$tmp_dir" # tmp-dir
#echo "$tree_file" # tree-file
#echo "$alignment_file" # alignment-file
#echo "$sequence_file" # new sequences
#echo "$log_file" # log-file

# Run mafft profile alignment
/usr/local/bin/mafft --add "$sequence_file" --keeplength --thread -1 "$tmp_dir/alignment.short.fasta" > "$tmp_dir/alignment.block.fasta"
# Bring everything to one line
sed -e 's/\(^>.*$\)/#\1#/' "$tmp_dir/alignment.block.fasta" | tr -d "\r" | tr -d "\n" | sed -e 's/$/#/' | tr "#" "\n" | sed -e '/^$/d' > "$tmp_dir/alignment.full.fasta"
# Write the new sequence to the file sequences.fasta
tail -n 2 "$tmp_dir/alignment.full.fasta" > "$tmp_dir/sequences.fasta"

# Create reference package
/usr/local/bin/taxit create -a Rega -P "$tmp_dir/reference.refpkg" -l HIV-1 --aln-fasta "$alignment_file" --tree-stats "$log_file" --tree-file "$tree_file" 2>> $monitor_file

echo "Reference package created"
echo "Starting pplacing"

# Copy newly added sequences to tmp dir
# cp $sequence_file $tmp_dir 

# Start PPlacer
echo "/Users/phylogeo/Documents/pplacer-Darwin-v1.1.alpha17-6-g5cecf99/pplacer --mmap-file $tmp_dir --out-dir $tmp_dir -c $tmp_dir/reference.refpkg $sequence_file"
/Users/phylogeo/Documents/pplacer-Darwin-v1.1.alpha17-6-g5cecf99/pplacer --mmap-file "$tmp_dir" --out-dir "$tmp_dir" -c "$tmp_dir/reference.refpkg" -o "$tmp_dir/sequences.jplace" "$tmp_dir/sequences.fasta"
echo "PPlacing done"
echo "Creating tree"

#Start Guppy
/Users/phylogeo/Documents/pplacer-Darwin-v1.1.alpha17-6-g5cecf99/guppy tog --out-dir "$tmp_dir" -o sequences.tog.tre "$tmp_dir/sequences.jplace" 2>> $monitor_file

echo "Tree created at: $tmp_dir/sequences.tog.tre"
